package dev.mvrk.blog.dto.response;

import dev.mvrk.blog.repository.projection.PostCardView;

import java.time.LocalDateTime;

public record PostCardResponseDto(
        Long id,
        String title,
        String shortDescription,
        String author,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        Long commentsCount
) {
    public static PostCardResponseDto fromProjection(PostCardView projection) {
        return new PostCardResponseDto(
                projection.getId(),
                projection.getTitle(),
                projection.getShortDescription(),
                projection.getAuthorNickname(),
                projection.getCreatedAt(),
                projection.getUpdatedAt(),
                projection.getCommentsCount()
        );
    }
}
