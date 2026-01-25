package dev.mvrk.blog.security.oauth2;

import dev.mvrk.blog.dto.OAuth2UserDto;
import dev.mvrk.blog.entity.User;
import dev.mvrk.blog.entity.enums.AuthProvider;
import dev.mvrk.blog.entity.enums.Role;
import dev.mvrk.blog.repository.UserRepository;
import dev.mvrk.blog.security.details.CustomUserDetails;
import dev.mvrk.blog.security.jwt.JwtUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NullMarked;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final JwtUtils jwtUtils;
    private final UserRepository userRepository;
    private final OAuth2StrategyFactory strategyFactory;

    @Value("${application.redirect-uri}")
    private String redirectUri;
    private static final String TEMP_EMAIL_DOMAIN = "@no-email.temp";


    @Override
    @NullMarked
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        OAuth2AuthenticationToken token = (OAuth2AuthenticationToken) authentication;
        OAuth2User oAuth2User = token.getPrincipal();
        String providerId = token.getAuthorizedClientRegistrationId();

        OAuth2UserDto userInfo = strategyFactory.getStrategy(providerId).extract(oAuth2User);
        AuthProvider providerEnum = AuthProvider.valueOf(providerId.toUpperCase());

        User user = resolveUser(userInfo, providerEnum);

        String jwt = jwtUtils.generateJwtToken(new CustomUserDetails(user));
        sendRedirect(request, response, jwt, user);
    }

    private User resolveUser(OAuth2UserDto authDto, AuthProvider provider) {
        Optional<User> byProvider = userRepository.findByAuthProviderAndProviderId(provider, authDto.providerId());
        if (byProvider.isPresent()) {
            return byProvider.get();
        }

        Optional<User> byEmail = userRepository.findByEmail(authDto.email());
        if (byEmail.isPresent()) {
            User user = byEmail.get();
            user.setAuthProvider(provider);
            user.setProviderId(authDto.providerId());
            return userRepository.save(user);
        }

        return registerNewUser(authDto, provider);
    }

    private User registerNewUser(OAuth2UserDto authDto, AuthProvider provider) {
        User user = new User();
        user.setEmail(authDto.email());
        user.setAuthProvider(provider);
        user.setProviderId(authDto.providerId());
        user.setRole(Collections.singleton(Role.ROLE_USER));
        user.setNickname(authDto.nickname());
        user.setUsername(ensureUniqueUsername(authDto.username()));

        return userRepository.save(user);
    }

    private void sendRedirect(HttpServletRequest request, HttpServletResponse response, String token, User user) throws IOException {
        boolean isPlaceholder = user.getEmail().endsWith(TEMP_EMAIL_DOMAIN);

        UriComponentsBuilder targetUrl = UriComponentsBuilder.fromUriString(redirectUri)
                .queryParam("token", token)
                .queryParamIfPresent("needs_email", isPlaceholder ? Optional.of(true) : Optional.empty());

        getRedirectStrategy().sendRedirect(request, response, targetUrl.build().toUriString());
    }

    private String ensureUniqueUsername(String username) {
        if (!userRepository.existsUserByUsername(username)) {
            return username;
        }
        return username + "_" + UUID.randomUUID().toString().substring(0, 8);
    }
}
