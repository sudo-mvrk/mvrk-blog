package dev.mvrk.blog.repository;

import dev.mvrk.blog.entity.Post;
import dev.mvrk.blog.repository.projection.PostCardView;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface PostRepository extends JpaRepository<Post, Long> {
    @Query(
            "select " +
                    "p.id as id, " +
                    "p.title as title, " +
                    "p.shortDescription as shortDescription, " +
                    "p.createdAt as createdAt, " +
                    "p.updatedAt as updateAt, " +
                    "u.nickname as authorNickname, " +
                    "count(c) as commentsCount " +
                    "from Post p " +
                    "left join p.comments c " +
                    "left join p.author u " +
                    "group by p.id, p.title, p.shortDescription, u.nickname, p.createdAt, p.updatedAt"
    )
    Page<PostCardView> findBy(Pageable pageable);

    boolean existsById(Long id);
}
