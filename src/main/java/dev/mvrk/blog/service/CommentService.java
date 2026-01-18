package dev.mvrk.blog.service;

import dev.mvrk.blog.dto.request.CommentRequestDto;
import dev.mvrk.blog.entity.Comment;
import dev.mvrk.blog.entity.Post;
import dev.mvrk.blog.entity.User;
import dev.mvrk.blog.exception.DataNotFoundException;
import dev.mvrk.blog.exception.PermissionDeniedException;
import dev.mvrk.blog.exception.UserNotFoundException;
import dev.mvrk.blog.repository.CommentRepository;
import dev.mvrk.blog.repository.PostRepository;
import dev.mvrk.blog.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NullMarked;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static dev.mvrk.blog.utils.PatchUtils.updateIfPresent;


@Service
@RequiredArgsConstructor
public class CommentService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;

    @Transactional
    public Comment createComment(Long id, String username, CommentRequestDto requestDto) {
        Post postById = findPostByIdOrElseThrow(id);

        User author = findUserByUsernameOrElseThrow(username);

        Comment comment = Comment.builder()
                .text(requestDto.text())
                .author(author)
                .post(postById)
                .build();
        return commentRepository.save(comment);
    }

    @Transactional
    public Comment updateComment(Long commentId, String username, CommentRequestDto requestDto) {
        Comment comment = findCommentByIdOrElseThrow(commentId);
        if (!username.equals(comment.getAuthor().getUsername())) {
            throw new PermissionDeniedException("You are not allowed to change that comment");
        }
        updateIfPresent(requestDto.text(), comment::setText);
        return commentRepository.save(comment);
    }

    @NullMarked
    public Page<Comment> getCommentsForPost(Long postId, Pageable pageable) {
        if (!postRepository.existsById(postId)) {
            throw new DataNotFoundException("Post with id " + postId + " not found");
        }
        return commentRepository.findAllByPost_Id(postId, pageable);
    }

    public void deleteCommentById(Long commentId, String username, boolean isAdmin) {
        Comment comment = findCommentByIdOrElseThrow(commentId);
        if (!username.equals(comment.getAuthor().getUsername()) || !isAdmin) {
            throw new PermissionDeniedException("You are not allowed to remove that comment");
        }
        commentRepository.delete(comment);
    }

    public Long getCommentsCountByPostId(Long postId) {
        return commentRepository.countCommentByPostId(postId);
    }

    private Comment findCommentByIdOrElseThrow(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new DataNotFoundException("Comment with id " + commentId + " not found"));
    }

    private User findUserByUsernameOrElseThrow(String username) {
        return userRepository.findUserByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User with username " + username + " not found"));
    }

    private Post findPostByIdOrElseThrow(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new DataNotFoundException("Post with id " + postId + " not found"));
    }
}
