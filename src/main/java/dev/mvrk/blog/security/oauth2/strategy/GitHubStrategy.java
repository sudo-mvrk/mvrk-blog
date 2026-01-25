package dev.mvrk.blog.security.oauth2.strategy;

import dev.mvrk.blog.dto.OAuth2UserDto;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

@Component
public class GitHubStrategy implements OAuth2UserStrategy {
    private static final String TEMP_EMAIL_DOMAIN = "@no-email.temp";

    @Override
    public String getProviderName() {
        return "github";
    }

    @Override
    public OAuth2UserDto extract(OAuth2User user) {
        Object idAttribute = user.getAttribute("id");
        String providerId = String.valueOf(idAttribute);

        String login = user.getAttribute("login");
        String email = user.getAttribute("email");

        if (email == null) {
            email = login + TEMP_EMAIL_DOMAIN;
        }

        return new OAuth2UserDto(
                providerId,
                email,
                user.getAttribute("name") != null ? user.getAttribute("name") : login,
                login
        );
    }
}
