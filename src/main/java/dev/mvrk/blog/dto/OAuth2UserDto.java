package dev.mvrk.blog.dto;

import lombok.Builder;

@Builder
public record OAuth2UserDto(
        String providerId,
        String email,
        String nickname,
        String username
) {
}
