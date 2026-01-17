package com.example.mvrkblog.dto.response;

import com.example.mvrkblog.entity.User;

import java.time.LocalDateTime;
import java.util.List;

public record UserResponseDto(Long id,
                              String username,
                              List<String> role,
                              String nickname,
                              String email,
                              String about,
                              String avatarUrl,
                              LocalDateTime createdAt) {
    public static UserResponseDto fromEntity(User user) {
        return new UserResponseDto(
                user.getId(),
                user.getUsername(),
                user.getRole().stream().map(Enum::name).toList(),
                user.getNickname(),
                user.getEmail(),
                user.getAbout(),
                user.getAvatarUrl(),
                user.getCreatedAt()
        );
    }
}
