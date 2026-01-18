package dev.mvrk.blog.controller;

import dev.mvrk.blog.dto.request.PatchPostRequestDto;
import dev.mvrk.blog.dto.request.PostRequestDto;
import dev.mvrk.blog.dto.response.PostCardResponseDto;
import dev.mvrk.blog.dto.response.PostResponseDto;
import dev.mvrk.blog.service.CommentService;
import dev.mvrk.blog.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;
    private final CommentService commentService;

    @GetMapping
    public Page<PostCardResponseDto> getAllPosts(@PageableDefault Pageable pageable) {
        return postService.getAllPosts(pageable).map(PostCardResponseDto::fromProjection);
    }

    @GetMapping("/{id}")
    public PostResponseDto getPostById(@PathVariable Long id) {
        return PostResponseDto.fromEntity(postService.getPostById(id), getCommentsCountByPostId(id));
    }


    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public PostResponseDto createPost(@RequestBody @Valid PostRequestDto requestDto, Principal principal) {
        return PostResponseDto.fromEntity(postService.createPost(requestDto, principal.getName()), 0L);
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public PostResponseDto updatePost(@PathVariable Long id,
                                      @RequestBody PatchPostRequestDto requestDto,
                                      Principal principal) {
        return PostResponseDto.fromEntity(postService.updatePost(id, requestDto, principal.getName()), getCommentsCountByPostId(id));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePostById(@PathVariable Long id) {
        postService.deletePostById(id);
    }

    private Long getCommentsCountByPostId(Long id) {
        return commentService.getCommentsCountByPostId(id);
    }
}
