package com.ylab.walletservice.configuration;

import com.ylab.walletservice.aop.aspects.LoggableAspect;
import com.ylab.walletservice.aop.aspects.PlayerAuditAspect;
import com.ylab.walletservice.aop.aspects.TransactionAuditAspect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * Configuration class for setting up Aspect-Oriented Programming (AOP) components.
 * Configures and registers various AOP aspects.
 */
@Configuration
@EnableAspectJAutoProxy
public class Aop {
    /**
     * Bean definition for PlayerAuditAspect.
     *
     * @return an instance of PlayerAuditAspect
     */
    @Bean
    PlayerAuditAspect playerAuditAspect(){
        return new PlayerAuditAspect();
    }

    /**
     * Bean definition for TransactionAuditAspect.
     *
     * @return an instance of TransactionAuditAspect
     */
    @Bean
    TransactionAuditAspect transactionAuditAspect(){
        return new TransactionAuditAspect();
    }

    /**
     * Bean definition for LoggableAspect.
     *
     * @return an instance of LoggableAspect
     */
    @Bean
    LoggableAspect loggableAspect(){
        return new LoggableAspect();
    }
}