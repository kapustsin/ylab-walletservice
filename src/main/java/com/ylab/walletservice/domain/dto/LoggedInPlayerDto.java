package com.ylab.walletservice.domain.dto;

/**
 * DTO class representing a logged-in player.
 * Encapsulates the information about a player who is currently logged in.
 */
public record LoggedInPlayerDto(long id, String login) {
}