package dev.mvrk.blog.dto.response;

import dev.mvrk.blog.entity.Post;

import java.time.LocalDateTime;

public record PostResponseDto(
        Long id,
        String title,
        String shortDescription,
        String content,
        String author,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        Long commentsCount
) {
    public static PostResponseDto fromEntity(Post post, Long commentsCount) {
        return new PostResponseDto(
                post.getId(),
                post.getTitle(),
                post.getShortDescription(),
                post.getContent(),
                post.getAuthor().getNickname(),
                post.getCreatedAt(),
                post.getUpdatedAt(),
                commentsCount
        );
    }
}
