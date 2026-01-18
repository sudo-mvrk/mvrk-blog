package dev.mvrk.blog.controller;

import dev.mvrk.blog.dto.request.AuthRequestDto;
import dev.mvrk.blog.dto.request.RegistrationRequestDto;
import dev.mvrk.blog.dto.response.AuthResponseDto;
import dev.mvrk.blog.entity.User;
import dev.mvrk.blog.security.details.CustomUserDetails;
import dev.mvrk.blog.security.jwt.JwtUtils;
import dev.mvrk.blog.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final JwtUtils jwtUtils;
    private final UserService userService;
    private final AuthenticationManager authenticationManager;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public AuthResponseDto register(@RequestBody @Valid RegistrationRequestDto registrationRequestDto) {
        User registeredUser = userService.register(registrationRequestDto);
        String token = jwtUtils.generateJwtToken(new CustomUserDetails(registeredUser));
        return AuthResponseDto.fromEntity(registeredUser, token);
    }

    @PostMapping("/login")
    public AuthResponseDto login(@RequestBody @Valid AuthRequestDto authRequestDto) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                authRequestDto.username(),
                authRequestDto.password())
        );
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        User user = userDetails.getUser();
        String token = jwtUtils.generateJwtToken(userDetails);
        return AuthResponseDto.fromEntity(user, token);
    }
}
