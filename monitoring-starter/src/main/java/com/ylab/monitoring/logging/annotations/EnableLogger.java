package com.ylab.monitoring.logging.annotations;

import com.ylab.monitoring.logging.configuration.AnnotationEnableLogger;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to enable logging.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(AnnotationEnableLogger.class)
public @interface EnableLogger {
}