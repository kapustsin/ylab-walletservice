package com.ylab.walletservice.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * Configuration class for configuring Swagger.
 */
@Configuration
@EnableOpenApi
public class Swagger {
    /**
     * Creates a Docket bean to configure Swagger for the API documentation using OpenAPI.
     *
     * @return A Docket instance with the specified configuration.
     */
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.OAS_30)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.ylab.walletservice"))
                .paths(PathSelectors.any())
                .build();
    }
}