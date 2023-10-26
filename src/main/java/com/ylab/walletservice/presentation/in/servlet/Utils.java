package com.ylab.walletservice.presentation.in.servlet;

import com.ylab.walletservice.domain.dto.CredentialsDto;
import com.ylab.walletservice.domain.dto.RegistrationDto;
import com.ylab.walletservice.domain.dto.TransactionRequestDto;

import java.math.BigDecimal;

/**
 * Utility class containing static methods to validate different types of data objects.
 */
public class Utils {
    /**
     * Validates the provided {@link CredentialsDto} object.
     *
     * @param dto The {@link CredentialsDto} object to be validated.
     * @return {@code true} if the credentials are valid, {@code false} otherwise.
     */
    static boolean isValid(CredentialsDto dto) {
        return dto != null &&
                dto.login() != null && !dto.login().trim().isEmpty() &&
                dto.password() != null && !dto.password().trim().isEmpty();
    }

    /**
     * Validates the provided {@link RegistrationDto} object.
     *
     * @param dto The {@link RegistrationDto} object to be validated.
     * @return {@code true} if the registration data is valid, {@code false} otherwise.
     */
    static boolean isValid(RegistrationDto dto) {
        return dto != null &&
                dto.login() != null && !dto.login().trim().isEmpty() &&
                dto.password() != null && !dto.password().trim().isEmpty();
    }

    /**
     * Validates the provided {@link TransactionRequestDto} object with the specified player ID.
     *
     * @param dto      The {@link TransactionRequestDto} object to be validated.
     * @param playerId The ID of the player associated with the transaction.
     * @return {@code true} if the transaction data is valid, {@code false} otherwise.
     */
    public static boolean isValid(TransactionRequestDto dto, long playerId) {
        return dto != null && dto.creatorId() == playerId &&
                dto.token() > 0 &&
                dto.creatorId() > 0 &&
                dto.amount() != null && dto.amount().compareTo(BigDecimal.ZERO) > 0 &&
                dto.type() != null && ("debit".equals(dto.type()) || "credit".equals(dto.type()));
    }
}