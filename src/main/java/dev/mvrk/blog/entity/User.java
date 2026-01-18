package dev.mvrk.blog.entity;

import dev.mvrk.blog.entity.enums.Role;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Table(name = "users")
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User extends BaseEntity {
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
    @Column(nullable = false)
    private Set<Role> role;
}
