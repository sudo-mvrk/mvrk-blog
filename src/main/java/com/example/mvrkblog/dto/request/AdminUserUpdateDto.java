package com.example.mvrkblog.dto.request;

import java.util.List;

public record AdminUserUpdateDto(
        String nickname,
        String about,
        String avatarUrl,
        Boolean isBanned,
        List<String> roles
) {
}
