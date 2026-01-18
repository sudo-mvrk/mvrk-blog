package dev.mvrk.blog.dto.response;

import dev.mvrk.blog.entity.User;

import java.time.LocalDateTime;
import java.util.List;

public record AuthResponseDto(Long id, String username, List<String> role, LocalDateTime createdAt, String token) {
    public static AuthResponseDto fromEntity(User user, String token) {
        return new AuthResponseDto(user.getId(),
                user.getUsername(),
                user.getRole().stream().map(Enum::name).toList(),
                user.getCreatedAt(),
                token);
    }
}
