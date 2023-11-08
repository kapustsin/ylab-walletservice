package com.ylab.monitoring.audit.service;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

/**
 * Aspect class for auditing transaction-related operations.
 * This aspect intercepts the execution of methods in the {@code TransactionService} class
 * and logs transaction creation attempts and history retrieval operations.
 */
@Aspect
public class TransactionAuditAspect {
    /**
     * Intercepts the execution of the {@code create} method in the {@code TransactionService} class
     * and logs the transaction creation results.
     *
     * @param joinPoint the join point representing the intercepted method execution
     * @return true if the transaction was successfully created, false otherwise
     * @throws Throwable if an error occurs during method execution
     */
    @Around("execution(* com.ylab.walletservice.service.TransactionService.create(..))")
    public boolean createAspect(ProceedingJoinPoint joinPoint) throws Throwable {
        boolean result = (boolean) joinPoint.proceed();
        if (result) {
            System.out.println("Transaction created successfully!");
        } else {
            System.out.println("Transaction created failed!");
        }
        return result;
    }

    /**
     * Intercepts the execution of the {@code getHistory} method in the {@code TransactionService} class
     * and logs the transaction history retrieval operation.
     *
     * @param joinPoint the join point representing the intercepted method execution
     * @return the transaction history of the specified player
     * @throws Throwable if an error occurs during method execution
     */
    @Around("execution(* com.ylab.walletservice.service.TransactionService.getHistory(..))")
    public Object getHistoryAspect(ProceedingJoinPoint joinPoint) throws Throwable {
        System.out.println("Player is retrieving transaction history.");
        return joinPoint.proceed();
    }
}