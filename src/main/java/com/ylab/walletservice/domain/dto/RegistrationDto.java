package com.ylab.walletservice.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegistrationDto(
        @NotBlank(message = "Login cannot be blank")
        @Size(min = 3, max = 20, message = "Login must be between 3 and 20 characters")
        String login,
        @NotBlank(message = "Password cannot be blank")
        @Size(min = 6, max = 30, message = "Password must be between 6 and 30 characters")
        String password) {
}
