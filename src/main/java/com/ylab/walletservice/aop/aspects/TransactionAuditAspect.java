package com.ylab.walletservice.aop.aspects;

import com.ylab.walletservice.domain.Transaction;
import com.ylab.walletservice.domain.dto.TransactionRequestDto;
import com.ylab.walletservice.service.LogService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

import java.util.List;

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
     * @param joinPoint          the join point representing the intercepted method execution
     * @param transactionRequest the transaction request data for creating a new transaction
     * @return true if the transaction was successfully created, false otherwise
     * @throws Throwable if an error occurs during method execution
     */
    @Around("execution(* com.ylab.walletservice.service.TransactionService.create(..)) && args(transactionRequest)")
    public boolean createAspect(ProceedingJoinPoint joinPoint, TransactionRequestDto transactionRequest)
            throws Throwable {
        boolean result = (boolean) joinPoint.proceed();
        if (result) {
            LogService.add("Transaction created successfully. Token: " + transactionRequest.token() +
                    ", Creator ID: " + transactionRequest.creatorId() +
                    ", Amount: " + transactionRequest.amount() +
                    ", Type: " + transactionRequest.type());
        } else {
            LogService.add("Transaction creation failed. Token: " + transactionRequest.token() +
                    ", Creator ID: " + transactionRequest.creatorId() +
                    ", Amount: " + transactionRequest.amount() +
                    ", Type: " + transactionRequest.type());
        }
        return result;
    }

    /**
     * Intercepts the execution of the {@code getHistory} method in the {@code TransactionService} class
     * and logs the transaction history retrieval operation.
     *
     * @param joinPoint the join point representing the intercepted method execution
     * @param playerId  the ID of the player whose transaction history is being retrieved
     * @return the transaction history of the specified player
     * @throws Throwable if an error occurs during method execution
     */
    @Around("execution(* com.ylab.walletservice.service.TransactionService.getHistory(long)) && args(playerId)")
    public List<Transaction> getHistoryAspect(ProceedingJoinPoint joinPoint, long playerId) throws Throwable {
        LogService.add("User with ID " + playerId + " is retrieving transaction history.");
        return (List<Transaction>) joinPoint.proceed();
    }
}