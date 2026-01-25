package dev.mvrk.blog.security.oauth2.strategy;

import dev.mvrk.blog.dto.OAuth2UserDto;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

@Component
public class GoogleStrategy implements OAuth2UserStrategy {
    @Override
    public String getProviderName() {
        return "google";
    }

    @Override
    public OAuth2UserDto extract(OAuth2User user) {
        Object emailAttribute = user.getAttribute("email");
        String email = String.valueOf(emailAttribute).split("@")[0];
        return new OAuth2UserDto(
                user.getAttribute("sub"),
                user.getAttribute("email"),
                user.getAttribute("name"),
                email
        );

    }
}
