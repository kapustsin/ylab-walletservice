package com.ylab.walletservice.aop.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to mark methods and classes for logging.
 * Methods and classes annotated with {@code @Loggable} will be intercepted
 * by the {@link com.ylab.walletservice.aop.aspects.LoggableAspect} for logging purposes.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface Loggable {
}