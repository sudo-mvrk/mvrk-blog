package dev.mvrk.blog.dto.response;

import dev.mvrk.blog.entity.Comment;

import java.time.LocalDateTime;

public record CommentResponseDto(
        Long commentId,
        String text,
        String author,
        Long postId,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static CommentResponseDto fromEntity(Comment comment) {
        return new CommentResponseDto(
                comment.getId(),
                comment.getText(),
                comment.getAuthor().getNickname(),
                comment.getPost().getId(),
                comment.getCreatedAt(),
                comment.getUpdatedAt()
        );
    }
}
