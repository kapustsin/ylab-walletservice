package com.ylab.walletservice.domain.dto;

import java.math.BigDecimal;

/**
 * A DTO class representing a transaction request.
 */
public record TransactionRequestDto(long token, long creatorId, BigDecimal amount, String type) {
}