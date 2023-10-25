package com.ylab.walletservice.presentation.in.servlet;

import com.ylab.walletservice.domain.dto.CredentialsDto;
import com.ylab.walletservice.domain.dto.RegistrationDto;
import com.ylab.walletservice.domain.dto.TransactionRequestDto;

import java.math.BigDecimal;

public class Utils {
    static boolean isValid(CredentialsDto dto) {
        return dto != null &&
                dto.login() != null && !dto.login().trim().isEmpty() &&
                dto.password() != null && !dto.password().trim().isEmpty();
    }

    static boolean isValid(RegistrationDto dto) {
        return dto != null &&
                dto.login() != null && !dto.login().trim().isEmpty() &&
                dto.password() != null && !dto.password().trim().isEmpty();
    }

    public static boolean isValid(TransactionRequestDto dto, long playerId) {
        return dto != null && dto.creatorId() == playerId &&
                dto.token() > 0 &&
                dto.creatorId() > 0 &&
                dto.amount() != null && dto.amount().compareTo(BigDecimal.ZERO) > 0 &&
                dto.type() != null && ("debit".equals(dto.type()) || "credit".equals(dto.type()));
    }
}