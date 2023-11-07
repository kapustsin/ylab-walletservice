package com.ylab.walletservice.configuration;

import com.ylab.walletservice.presentation.handler.TokenValidationInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configuration class for setting up Spring MVC.
 */
@Configuration
public class Mvc implements WebMvcConfigurer {
    private final TokenValidationInterceptor tokenValidationInterceptor;

    /**
     * Constructor for the Mvc class.
     *
     * @param tokenValidationInterceptor Interceptor for token validation.
     */
    public Mvc(TokenValidationInterceptor tokenValidationInterceptor) {
        this.tokenValidationInterceptor = tokenValidationInterceptor;
    }

    /**
     * Adds interceptors for handling incoming requests.
     *
     * @param registry InterceptorRegistry object.
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(tokenValidationInterceptor)
                .addPathPatterns("/api/**")
                .excludePathPatterns("/api/player/registration", "/api/player/authorisation");
    }
}