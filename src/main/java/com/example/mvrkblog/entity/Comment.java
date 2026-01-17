package com.example.mvrkblog.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "comment")
@Getter
@Setter
@ToString(exclude = {"post"})
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "text")
    private String text;
    @ManyToOne
    private User author;
    @ManyToOne(fetch = FetchType.LAZY)
    private Post post;
    @CreationTimestamp
    @Column(name = "createdAt")
    private LocalDateTime createdAt;
}
