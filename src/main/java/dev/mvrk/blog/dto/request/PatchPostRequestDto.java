package dev.mvrk.blog.dto.request;

public record PatchPostRequestDto(
        String title,
        String shortDescription,
        String content
) {
}
