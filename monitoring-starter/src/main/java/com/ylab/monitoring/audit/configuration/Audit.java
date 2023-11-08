package com.ylab.monitoring.audit.configuration;

import com.ylab.monitoring.audit.service.PlayerAuditAspect;
import com.ylab.monitoring.audit.service.TransactionAuditAspect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Audit {
    @Bean
    PlayerAuditAspect playerAuditAspect() {
        return new PlayerAuditAspect();
    }

    @Bean
    TransactionAuditAspect transactionAuditAspect() {
        return new TransactionAuditAspect();
    }
}