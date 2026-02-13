package dev.mvrk.blog.entity;

import dev.mvrk.blog.entity.enums.AuthProvider;
import dev.mvrk.blog.entity.enums.Role;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Table(name = "users", indexes = {
        @Index(name = "idx_provider_user", columnList = "authProvider, providerId")
})
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
    @Column(name = "password")
    private String password;
    @Column(name = "authProvider")
    private AuthProvider authProvider;
    @Column(name = "providerId", unique = true)
    private String providerId;
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
    @CollectionTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"))
    @Column(nullable = false)
    private Set<Role> role;
}
