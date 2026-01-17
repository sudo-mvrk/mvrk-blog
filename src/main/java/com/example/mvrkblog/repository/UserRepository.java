package com.example.mvrkblog.repository;

import com.example.mvrkblog.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsUserByUsername(String username);
    boolean existsUserByEmail(String email);
    Optional<User> findUserByUsername(String username);
}
