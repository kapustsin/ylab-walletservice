package com.ylab.walletservice.aop.aspects;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

/**
 * Aspect for logging method execution time.
 * This aspect intercepts methods annotated with {@link com.ylab.walletservice.aop.annotations.Loggable}
 * and logs the method invocation and execution time.
 */
@Aspect
public class LoggableAspect {
    /**
     * Pointcut expression to match methods annotated with {@link com.ylab.walletservice.aop.annotations.Loggable}.
     */
    @Pointcut("within(@com.ylab.walletservice.aop.annotations.Loggable *) && execution(* *(..))")
    public void annotatedByLoggable() {
    }

    /**
     * Advice to log method invocation and execution time for methods matching the pointcut expression.
     *
     * @param proceedingJoinPoint the join point representing the intercepted method execution
     * @return the result of the intercepted method execution
     * @throws Throwable if an error occurs during method execution
     */
    @Around("annotatedByLoggable()")
    public Object logging(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        System.out.println("Calling method " + proceedingJoinPoint.getSignature());
        long startTime = System.currentTimeMillis();
        Object result = proceedingJoinPoint.proceed();
        long endTime = System.currentTimeMillis();
        System.out.println("Execution of method " + proceedingJoinPoint.getSignature() +
                           " finished. Execution time is " + (endTime - startTime) + " ms");
        return result;
    }
}