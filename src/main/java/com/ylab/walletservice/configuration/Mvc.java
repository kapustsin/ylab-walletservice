package com.ylab.walletservice.configuration;

import com.ylab.walletservice.presentation.handler.TokenValidationInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
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

    /**
     * Configures resource handlers for Swagger UI.
     *
     * @param registry The resource handler registry to which settings for Swagger UI are added.
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/swagger-ui/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/springfox-swagger-ui/")
                .resourceChain(false);
    }

    /**
     * Configures view controllers for Swagger UI.
     *
     * @param registry The view controller registry to which settings for Swagger UI are added.
     */
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/swagger-ui/")
                .setViewName("forward:" + "/swagger-ui/index.html");
    }
}