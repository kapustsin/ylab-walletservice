package com.ylab.walletservice;

import com.ylab.monitoring.logging.annotations.EnableLogger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableLogger
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}