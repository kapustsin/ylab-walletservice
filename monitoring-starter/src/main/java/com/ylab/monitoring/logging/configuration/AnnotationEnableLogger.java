package com.ylab.monitoring.logging.configuration;

import com.ylab.monitoring.logging.service.LogService;
import com.ylab.monitoring.logging.service.LoggerAspect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AnnotationEnableLogger {
    @Bean
    LoggerAspect loggerAspect() {
        return new LoggerAspect();
    }
}