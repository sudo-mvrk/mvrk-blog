package com.example.mvrkblog.entity;

import com.example.mvrkblog.entity.enums.Role;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "users")
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "username", nullable = false, unique = true)
    private String username;
    @Column(name = "password", nullable = false)
    private String password;
    private String nickname;
    @Column(unique = true)
    private String email;
    @Lob
    private String about;
    private String avatarUrl;
    @Builder.Default
    @Column(nullable = false)
    private Boolean isBanned = false;
    @Enumerated(EnumType.STRING)
    @ElementCollection(targetClass = Role.class)
    @Column(name = "role", nullable = false)
    private Set<Role> role;
    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
