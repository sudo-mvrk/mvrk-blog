package dev.mvrk.blog.controller;

import dev.mvrk.blog.dto.request.CommentRequestDto;
import dev.mvrk.blog.dto.response.CommentResponseDto;
import dev.mvrk.blog.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @PatchMapping("/comments/{id}")
    public CommentResponseDto updateComment(@PathVariable Long id,
                                            @RequestBody @Valid CommentRequestDto requestDto,
                                            Principal principal
    ) {
        return CommentResponseDto.fromEntity(commentService.updateComment(id, principal.getName(), requestDto));
    }

    @DeleteMapping("/comments/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteComment(@PathVariable Long id,
                              Authentication authentication) {
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> "ROLE_ADMIN".equals(a.getAuthority()));
        commentService.deleteCommentById(id, authentication.getName(), isAdmin);
    }

    @PostMapping("/posts/{postId}/comments")
    public CommentResponseDto createComment(@PathVariable Long postId,
                                            @RequestBody @Valid CommentRequestDto requestDto,
                                            Principal principal) {
        return CommentResponseDto.fromEntity(commentService.createComment(postId, principal.getName(), requestDto));
    }

    @GetMapping("/posts/{id}/comments")
    public Page<CommentResponseDto> getAllComments(@PathVariable Long id,
                                                   @PageableDefault(size = 20) Pageable pageable) {
        return commentService.getCommentsForPost(id, pageable).map(CommentResponseDto::fromEntity);
    }
}
