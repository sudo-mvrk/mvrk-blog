package com.example.mvrkblog.controller;

import com.example.mvrkblog.dto.request.AdminUserUpdateDto;
import com.example.mvrkblog.dto.request.ProfileUpdateRequestDto;
import com.example.mvrkblog.dto.response.UserResponseDto;
import com.example.mvrkblog.entity.User;
import com.example.mvrkblog.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @PreAuthorize("hasRole('ADMIN')")
    public Page<UserResponseDto> getAllUsers(@PageableDefault(size = 20) Pageable pageable){
        return userService.getAllUsers(pageable).map(UserResponseDto::fromEntity);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public UserResponseDto updateUserByAdmin(@PathVariable Long id, @RequestBody AdminUserUpdateDto requestDto) {
        User user = userService.updateUserProfileByAdmin(id, requestDto);
        return UserResponseDto.fromEntity(user);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUserByAdmin(@PathVariable Long id) {
        userService.deleteUserByAdmin(id);
    }
}
