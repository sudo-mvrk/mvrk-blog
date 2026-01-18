package dev.mvrk.blog.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record PostRequestDto(
        @NotBlank(message = "Title is required")
        @Size(min = 3, max = 200, message = "Title must be between 3 and 200 characters")
        String title,
        @NotBlank(message = "Short description is required")
        @Size(min = 3, max = 200, message = "Short description must be between 3 and 200 characters")
        String shortDescription,
        @NotBlank(message = "Content is required")
        String content,
        String imageUrl) {
}
