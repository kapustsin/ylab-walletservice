package com.ylab.monitoring.logging.service;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

import java.math.BigDecimal;
import java.util.Optional;

@Aspect
public class LoggerAspect {

    /**
     * Pointcut expression to match methods annotated with @EnableLogger}.
     */
    @Pointcut("within(@com.ylab.monitoring.logging.annotations.EnableLogger *) && execution(* *(..))")
    public void annotatedByLoggable() {
    }

    /**
     * Aspect for logging authentication attempts.
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
            LogService.add("Success authorization.");
        } else {
            LogService.add("Authorization failed.");
        }
        return player;
    }

    /**
     * Aspect for logging player creation attempts.
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
            LogService.add("New player creation succeeded.");
        } else {
            LogService.add("New player creation failed.");
        }
        return result;
    }

    /**
     * Aspect for logging player balance check attempts.
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
        LogService.add("Player checked balance.");
        return balance;
    }

    /**
     * Intercepts the execution of the {@code create} method in the {@code TransactionService} class
     * and logs the transaction creation results.
     *
     * @param joinPoint the join point representing the intercepted method execution
     * @return true if the transaction was successfully created, false otherwise
     * @throws Throwable if an error occurs during method execution
     */
    @Around("execution(* com.ylab.walletservice.service.TransactionService.create(..))")
    public boolean createTransactionAspect(ProceedingJoinPoint joinPoint) throws Throwable {
        boolean result = (boolean) joinPoint.proceed();
        if (result) {
            LogService.add("Transaction created successfully!");
        } else {
            LogService.add("Transaction created failed!");
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
        LogService.add("Player is retrieving transaction history.");
        return joinPoint.proceed();
    }
}