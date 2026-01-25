package dev.mvrk.blog.controller;

import dev.mvrk.blog.dto.request.AdminUserUpdateDto;
import dev.mvrk.blog.dto.request.ProfileUpdateRequestDto;
import dev.mvrk.blog.dto.response.UserResponseDto;
import dev.mvrk.blog.entity.User;
import dev.mvrk.blog.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    @GetMapping("/me")
    public UserResponseDto getUserProfileByPrincipal(Principal principal) {
        User userProfile = userService.getUserProfile(principal.getName());
        return UserResponseDto.fromEntity(userProfile);
    }

    @PutMapping("/me")
    public UserResponseDto updateUserProfileByPrincipal(@RequestBody ProfileUpdateRequestDto requestDto, Principal principal) {
        return UserResponseDto.fromEntity(userService.updateMyProfile(principal.getName(), requestDto));
    }

    @DeleteMapping("/me")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUserProfileByPrincipal(Principal principal) {
        userService.deleteMyProfile(principal.getName());
    }

    @GetMapping("/{id}")
    public UserResponseDto getUserProfileById(@PathVariable Long id) {
        return UserResponseDto.fromEntity(userService.getUserProfileById(id));
    }

    @GetMapping
    public Page<UserResponseDto> getAllUsers(@PageableDefault(size = 20) Pageable pageable) {
        return userService.getAllUsers(pageable).map(UserResponseDto::fromEntity);
    }

    @PutMapping("/{id}")
    public UserResponseDto updateUserByAdmin(@PathVariable Long id, @RequestBody AdminUserUpdateDto requestDto) {
        User user = userService.updateUserProfileByAdmin(id, requestDto);
        return UserResponseDto.fromEntity(user);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUserByAdmin(@PathVariable Long id) {
        userService.deleteUserByAdmin(id);
    }
}
