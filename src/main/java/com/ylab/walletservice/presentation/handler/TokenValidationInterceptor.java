package com.ylab.walletservice.presentation.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ylab.walletservice.domain.dto.LoggedInPlayerDto;
import com.ylab.walletservice.service.utils.JwtService;
import io.jsonwebtoken.Claims;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Interceptor class responsible for validating JWT tokens in incoming requests.
 */
public class TokenValidationInterceptor implements HandlerInterceptor {
    private final JwtService jwtService;
    private final ObjectMapper objectMapper;

    /**
     * Constructs a new TokenValidationInterceptor with the specified JwtService and ObjectMapper instances.
     *
     * @param jwtService   The JwtService instance responsible for validating JWT tokens.
     * @param objectMapper The ObjectMapper instance used for JSON serialization.
     */
    public TokenValidationInterceptor(JwtService jwtService, ObjectMapper objectMapper) {
        this.jwtService = jwtService;
        this.objectMapper = objectMapper;
    }

    /**
     * Pre-handle method that intercepts incoming requests before they are handled by the controller.
     * It validates the JWT token present in the Authorization header.
     *
     * @param request  The {@link HttpServletRequest} object representing the request.
     * @param response The {@link HttpServletResponse} object representing the response.
     * @param handler  The handler (typically a controller method) to be executed.
     * @return true if the token is valid and the request should be processed; false otherwise.
     * @throws Exception If an error occurs during token validation or response writing.
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        String token = request.getHeader("Authorization").replace("Bearer ", "");

        if (jwtService.isValid(token)) {
            Claims claims = jwtService.extractClaim(token);
            request.setAttribute("player", new LoggedInPlayerDto(claims.get("id", Long.class), claims.getSubject()));
            return true;
        } else {
            response.setContentType("application/json");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getOutputStream().write(objectMapper.writeValueAsBytes("Authorization failed!"));
            return false;
        }
    }
}