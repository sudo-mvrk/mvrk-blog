package com.example.mvrkblog.service;

import com.example.mvrkblog.dto.request.AdminUserUpdateDto;
import com.example.mvrkblog.dto.request.ProfileUpdateRequestDto;
import com.example.mvrkblog.dto.request.RegistrationRequestDto;
import com.example.mvrkblog.dto.response.UserResponseDto;
import com.example.mvrkblog.entity.User;
import com.example.mvrkblog.entity.enums.Role;
import com.example.mvrkblog.exception.UserAlreadyExistsException;
import com.example.mvrkblog.exception.UserNotFoundException;
import com.example.mvrkblog.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NullMarked;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User register(RegistrationRequestDto registrationRequestDto) {
        if (userRepository.existsUserByUsername(registrationRequestDto.username())) {
            throw new UserAlreadyExistsException("Username is taken");
        }
        if (userRepository.existsUserByEmail(registrationRequestDto.email())) {
            throw new UserAlreadyExistsException("Email already in use");
        }
        User user = User.builder()
                .username(registrationRequestDto.username())
                .password(passwordEncoder.encode(registrationRequestDto.password()))
                .email(registrationRequestDto.email())
                .role(new HashSet<>(Set.of(Role.ROLE_USER)))
                .build();
        return userRepository.save(user);
    }

    public User getUserProfile(String username) {
        return userRepository.findUserByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
    }

    @Transactional
    public User updateMyProfile(String username, ProfileUpdateRequestDto request) {
        User user = getUserProfile(username);

        updateIfPresent(request.nickname(), user::setNickname);
        updateIfPresent(request.about(), user::setAbout);
        updateIfPresent(request.avatarUrl(), user::setAvatarUrl);
        return userRepository.save(user);
    }

    @Transactional
    public void deleteMyProfile(String username) {
        User user = getUserProfile(username);
        userRepository.delete(user);
    }

    public User getUserProfileById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User not found"));
    }

    @NullMarked
    public Page<User> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    public User updateUserProfileByAdmin(Long id, AdminUserUpdateDto requestDto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User with id " + id + " was not found"));

        updateIfPresent(requestDto.nickname(), user::setNickname);
        updateIfPresent(requestDto.about(), user::setAbout);
        updateIfPresent(requestDto.avatarUrl(), user::setAvatarUrl);

        updateIfPresent(requestDto.isBanned(), user::setIsBanned);
        if (requestDto.roles() != null && !requestDto.roles().isEmpty()) {
            Set<Role> newRoles = requestDto.roles().stream()
                    .map(Role::valueOf)
                    .collect(Collectors.toSet());
            user.setRole(newRoles);
        }
        return userRepository.save(user);
    }
    @Transactional
    public void deleteUserByAdmin(Long id) {
        userRepository.deleteById(id);
    }


    private <T> void updateIfPresent(T value, Consumer<T> setter) {
        if (value != null) {
            setter.accept(value);
        }
    }
}
