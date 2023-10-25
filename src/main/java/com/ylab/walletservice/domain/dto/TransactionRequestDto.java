package com.ylab.walletservice.domain.dto;

import java.math.BigDecimal;

public record TransactionRequestDto(long token, long creatorId, BigDecimal amount, String type) {
}
