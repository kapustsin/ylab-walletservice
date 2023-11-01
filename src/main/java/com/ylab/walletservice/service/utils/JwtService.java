package com.ylab.walletservice.service.utils;

import com.ylab.walletservice.domain.dto.LoggedInPlayerDto;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.UUID;

import static java.time.Instant.now;

/**
 * Service for handling JSON Web Tokens in the application.
 */
public class JwtService {
    private static final String KEY = "8f7d3b21a4972c0f753f4b3b8c144e059d44e848a6c3ef478e72b670648ef42bc0b";
    private static final long TTL = 10;

    /**
     * Generates a JWT token for the given logged-in player.
     *
     * @param dto The logged-in player's data.
     * @return The generated JWT token.
     */
    public String generateToken(LoggedInPlayerDto dto) {
        return Jwts.builder()
                .claim("id", dto.id()).subject(dto.login()).id(UUID.randomUUID().toString()).issuedAt(Date.from(now()))
                .expiration(Date.from(now().plus(TTL, ChronoUnit.MINUTES)))
                .signWith(generateHmacKey())
                .compact();
    }

    /**
     * Checks if the provided token is valid.
     *
     * @param token The JWT token to validate.
     * @return True if the token is valid, false otherwise.
     */
    public boolean isValid(String token) {
        try {
            Claims claims = extractClaim(token);
            return token != null &&
                    !token.trim().isEmpty() && claims.getSubject() != null &&
                    claims.get("id", Long.class) != null;
        } catch (ExpiredJwtException e) {
            return false;
        } catch (JwtException e) {
            return false;
        }
    }

    /**
     * Extracts the claims from the JWT token.
     *
     * @param token The JWT token.
     * @return The extracted claims.
     */
    public Claims extractClaim(String token) {
        return Jwts.parser()
                .verifyWith(generateHmacKey())
                .build().parseSignedClaims(token).getPayload();
    }

    /**
     * Generates the HMAC key for signing and verifying JWT tokens.
     *
     * @return The HMAC key.
     */
    private SecretKey generateHmacKey() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(KEY));
    }
}