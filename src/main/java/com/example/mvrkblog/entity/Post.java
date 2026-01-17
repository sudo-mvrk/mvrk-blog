package com.example.mvrkblog.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Entity
@Table(name = "post")
@Getter
@Setter
@ToString(exclude = {"comments", "author"})
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String shortDescription;
    @Lob
    private String content;
    private String imageUrl;
    @ManyToOne(fetch = FetchType.LAZY)
    private User author;
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments;
}
