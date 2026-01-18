package dev.mvrk.blog.repository.projection;

import java.time.LocalDateTime;

public interface PostCardView {
    Long getId();

    String getTitle();

    String getShortDescription();

    String getAuthorNickname();

    LocalDateTime getCreatedAt();

    LocalDateTime getUpdatedAt();

    Long getCommentsCount();
}
