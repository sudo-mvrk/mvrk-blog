package dev.mvrk.blog.repository;

import dev.mvrk.blog.entity.User;
import dev.mvrk.blog.entity.enums.AuthProvider;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsUserByUsername(String username);

    boolean existsUserByEmail(String email);
    @EntityGraph(attributePaths = "role")
    Optional<User> findUserByUsername(String username);
    @EntityGraph(attributePaths = "role")
    Optional<User> findByEmail(String email);
    @EntityGraph(attributePaths = "role")
    Optional<User> findByAuthProviderAndProviderId(AuthProvider authProvider, String providerId);
}
