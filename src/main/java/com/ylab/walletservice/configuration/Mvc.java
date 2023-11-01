package com.ylab.walletservice.configuration;

import com.ylab.walletservice.presentation.handler.TokenValidationInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.HandlerTypePredicate;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * Configuration class for setting up Spring MVC.
 */
@Configuration
@EnableWebMvc
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
     * Configures custom message converters for the application.
     *
     * @param converters List of HttpMessageConverter objects.
     */
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder()
                .indentOutput(true);
        converters.add(new MappingJackson2HttpMessageConverter(builder.build()));
    }

    /**
     * Configures path matching options.
     *
     * @param configurer PathMatchConfigurer object.
     */
    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        configurer.addPathPrefix("/api", HandlerTypePredicate.forAnnotation(RestController.class));
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