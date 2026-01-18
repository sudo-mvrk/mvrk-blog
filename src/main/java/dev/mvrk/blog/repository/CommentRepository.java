package dev.mvrk.blog.repository;

import dev.mvrk.blog.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    // Optimization of N+1 problem, when we for every comment go to the DB to load information about Author
    @EntityGraph(attributePaths = {"author"})
    Page<Comment> findAllByPost_Id(Long id, Pageable pageable);

    long countCommentByPostId(Long id);
}
