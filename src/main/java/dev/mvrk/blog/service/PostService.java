package dev.mvrk.blog.service;

import dev.mvrk.blog.dto.request.PatchPostRequestDto;
import dev.mvrk.blog.dto.request.PostRequestDto;
import dev.mvrk.blog.entity.Post;
import dev.mvrk.blog.entity.User;
import dev.mvrk.blog.entity.enums.PostStatus;
import dev.mvrk.blog.exception.DataNotFoundException;
import dev.mvrk.blog.exception.PermissionDeniedException;
import dev.mvrk.blog.exception.UserNotFoundException;
import dev.mvrk.blog.repository.PostRepository;
import dev.mvrk.blog.repository.UserRepository;
import dev.mvrk.blog.repository.projection.PostCardView;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static dev.mvrk.blog.utils.PatchUtils.updateIfPresent;

@Service
@RequiredArgsConstructor
public class PostService {
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    @Transactional
    public Long draftPost(String username) {
        User user = getUserOrThrowUserNotFoundException(username);
        Post post = Post.builder()
                .title("Untitled")
                .author(user)
                .postStatus(PostStatus.DRAFT)
                .build();
        return postRepository.save(post).getId();
    }

    @Transactional
    public Post createPost(PostRequestDto postRequestDto, String username) {
        User user = getUserOrThrowUserNotFoundException(username);
        Post post = Post.builder()
                .title(postRequestDto.title())
                .shortDescription(postRequestDto.shortDescription())
                .content(postRequestDto.content())
                .author(user)
                .postStatus(PostStatus.PUBLISHED)
                .build();
        return postRepository.save(post);
    }

    private User getUserOrThrowUserNotFoundException(String username) {
        return userRepository.findUserByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
    }

    @Transactional
    public Post updatePost(Long id, PatchPostRequestDto requestDto, String name) {
        Post post = findPostOrThrow(id);

        if (!name.equals(post.getAuthor().getUsername())){
            throw new PermissionDeniedException("You are not allowed to change that post");
        }
        updateIfPresent(requestDto.title(), post::setTitle);
        updateIfPresent(requestDto.shortDescription(), post::setShortDescription);
        updateIfPresent(requestDto.content(), post::setContent);
        post.setPostStatus(PostStatus.PUBLISHED);
        return postRepository.save(post);
    }

    public Page<PostCardView> getAllPosts(Pageable pageable) {
        return postRepository.findBy(pageable);
    }

    public Post getPostById(Long id) {
        return findPostOrThrow(id);
    }

    @Transactional
    public void deletePostById(Long id) {
        Post post = findPostOrThrow(id);
        postRepository.delete(post);
    }

    private Post findPostOrThrow(Long id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Post with id " + id + " not found"));
    }
}
