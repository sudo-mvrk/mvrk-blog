package dev.mvrk.blog.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CommentRequestDto(
        @NotBlank
        @Size(min = 3, max = 200, message = "Text must be between 3 and 200 characters")
        String text
) {
}
