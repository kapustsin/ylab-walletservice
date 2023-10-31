package com.ylab.walletservice.domain.dto;

/**
 * DTO class representing registration data.
 */
public record RegistrationDto(
        String login,
        String password) {
}
