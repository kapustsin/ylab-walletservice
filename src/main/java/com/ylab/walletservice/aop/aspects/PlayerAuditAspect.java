package com.ylab.walletservice.aop.aspects;

import com.ylab.walletservice.domain.dto.CredentialsDto;
import com.ylab.walletservice.domain.dto.LoggedInPlayerDto;
import com.ylab.walletservice.domain.dto.RegistrationDto;
import com.ylab.walletservice.service.LogService;
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
     * Aspect for logging authentication attempts.
     * This aspect intercepts the execution of the {@code doAuthorisation} method in the
     * {@code PlayerService} class and logs the authentication results.
     *
     * @param joinPoint the join point representing the intercepted method execution
     * @return the result of the intercepted method execution
     * @throws Throwable if an error occurs during method execution
     */
    @Around("execution(* com.ylab.walletservice.service.PlayerService.doAuthorisation(..)) && args(credentials)")
    public Object doAuthorisationAspect(ProceedingJoinPoint joinPoint, CredentialsDto credentials) throws Throwable {
        Object player = joinPoint.proceed();
        if (player instanceof Optional && ((Optional<?>) player).isPresent()) {
            Optional<LoggedInPlayerDto> optionalResult = (Optional<LoggedInPlayerDto>) player;
            LogService.add("Player with login " + optionalResult.get().login() + "success authorization.");
        } else {
            LogService.add("Player with login " + credentials.login() + " authorization failed.");
        }
        return player;
    }

    /**
     * Aspect for logging player creation attempts.
     * This aspect intercepts the execution of the {@code create} method in the
     * {@code PlayerService} class and logs the creation results.
     *
     * @param registrationData the registration data for creating a new player
     * @return true if the player was successfully created, false otherwise
     */
    @Around("execution(* com.ylab.walletservice.service.PlayerService.create(..)) && args(registrationData)")
    public boolean createAspect(ProceedingJoinPoint joinPoint, RegistrationDto registrationData) throws Throwable {
        boolean result = (boolean) joinPoint.proceed();
        if (result) {
            LogService.add("New player creation with login " + registrationData.login() + " succeeded.");
        } else {
            LogService.add("New player creation with login " + registrationData.login() + " failed.");
        }
        return result;
    }

    /**
     * Aspect for logging player balance check attempts.
     * This aspect intercepts the execution of the {@code getBalance} method in the
     * {@code PlayerService} class and logs the balance check results.
     *
     * @param playerId the ID of the player whose balance is being checked
     * @return the balance of the specified player
     */
    @Around("execution(* com.ylab.walletservice.service.PlayerService.getBalance(long)) && args(playerId)")
    public BigDecimal getBalanceAspect(ProceedingJoinPoint joinPoint, long playerId) throws Throwable {
        System.out.println("Inside checkBalanceAspect!");
        BigDecimal balance = (BigDecimal) joinPoint.proceed();
        LogService.add("Player with id = " + playerId + " checked balance.");
        return balance;
    }
}