package com.example.mvrkblog.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record RegistrationRequestDto(
        @Email
        String email,
        @NotNull(message = "Username cannot be null")
        @Size(min = 3, max = 25, message = "Min size of username 3 symbols")
        String username,
        @NotNull(message = "Password cannot be null")
        @Size(min = 4, max = 25, message = "Min size of password 3 symbols")
        String password) {
}
