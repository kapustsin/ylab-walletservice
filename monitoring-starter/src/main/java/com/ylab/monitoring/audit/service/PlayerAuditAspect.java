package com.ylab.monitoring.audit.service;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

import java.math.BigDecimal;
import java.util.Optional;

/**
 * Aspect class for auditing player-related operations.
 * This aspect intercepts and logs the execution of methods in the {@code PlayerService} class
 * and logs authentication attempts, player creation, and balance check operations.
 */
@Aspect
public class PlayerAuditAspect {
    /**
     * Aspect for audit authentication attempts.
     * This aspect intercepts the execution of the {@code doAuthorisation} method in the
     * {@code PlayerService} class and logs the authentication results.
     *
     * @param joinPoint the join point representing the intercepted method execution
     * @return the result of the intercepted method execution
     * @throws Throwable if an error occurs during method execution
     */
    @Around("execution(* com.ylab.walletservice.service.PlayerService.doAuthorisation(..))")
    public Object doAuthorisationAspect(ProceedingJoinPoint joinPoint) throws Throwable {
        Object player = joinPoint.proceed();
        if (player instanceof Optional && ((Optional<?>) player).isPresent()) {
            System.out.println("Success authorization.");
        } else {
            System.out.println("Authorization failed.");
        }
        return player;
    }

    /**
     * Aspect for audit player creation attempts.
     * This aspect intercepts the execution of the {@code create} method in the
     * {@code PlayerService} class and logs the creation results.
     *
     * @param joinPoint the join point representing the intercepted method execution
     * @return true if the player was successfully created, false otherwise
     * @throws Throwable if an error occurs during method execution
     */
    @Around("execution(* com.ylab.walletservice.service.PlayerService.create(..))")
    public boolean createAspect(ProceedingJoinPoint joinPoint) throws Throwable {
        boolean result = (boolean) joinPoint.proceed();
        if (result) {
            System.out.println("New player creation succeeded.");
        } else {
            System.out.println("New player creation failed.");
        }
        return result;
    }

    /**
     * Aspect for audit player balance check attempts.
     * This aspect intercepts the execution of the {@code getBalance} method in the
     * {@code PlayerService} class and logs the balance check results.
     *
     * @param joinPoint the join point representing the intercepted method execution
     * @return the balance of the specified player
     * @throws Throwable if an error occurs during method execution
     */
    @Around("execution(* com.ylab.walletservice.service.PlayerService.getBalance(..))")
    public BigDecimal getBalanceAspect(ProceedingJoinPoint joinPoint) throws Throwable {
        BigDecimal balance = (BigDecimal) joinPoint.proceed();
        System.out.println("Player checked balance.");
        return balance;
    }
}