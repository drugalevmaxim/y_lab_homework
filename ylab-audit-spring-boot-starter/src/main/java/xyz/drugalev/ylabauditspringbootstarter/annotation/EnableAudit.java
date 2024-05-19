package xyz.drugalev.ylabauditspringbootstarter.annotation;

import org.springframework.context.annotation.Import;
import xyz.drugalev.ylabauditspringbootstarter.AuditConfig;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation for enabling audit functionality.
 *
 * @author Drugalev Maxim
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Import(AuditConfig.class)
public @interface EnableAudit {
}