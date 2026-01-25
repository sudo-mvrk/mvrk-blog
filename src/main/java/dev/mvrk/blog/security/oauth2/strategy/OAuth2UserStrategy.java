package dev.mvrk.blog.security.oauth2.strategy;

import dev.mvrk.blog.dto.OAuth2UserDto;
import org.springframework.security.oauth2.core.user.OAuth2User;

public interface OAuth2UserStrategy {
    String getProviderName();
    OAuth2UserDto extract(OAuth2User user);
}
