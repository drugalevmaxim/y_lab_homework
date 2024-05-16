package xyz.drugalev.ylablogspringbootstarter.annotation;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * Aspect for logging execution time of methods annotated with {@link xyz.drugalev.ylablogspringbootstarter.aspect.Loggable}.
 *
 * @author Drugalev Maxim
 */
@Aspect
@Component
@Slf4j
public class LogAspect {

    /**
     * Pointcut for methods annotated with {@link xyz.drugalev.ylablogspringbootstarter.aspect.Loggable}.
     */
    @Pointcut("@within(xyz.drugalev.ylablogspringbootstarter.aspect.Loggable) && execution(public * *(..))")
    private void annotatedWithLoggable() {
    }

    /**
     * Logs execution time of methods annotated with {@link xyz.drugalev.ylablogspringbootstarter.aspect.Loggable}.
     *
     * @param joinPoint the join point
     * @return the result of the method execution
     * @throws Throwable if an error occurs
     */
    @Around("annotatedWithLoggable()")
    public Object logExecution(ProceedingJoinPoint joinPoint) throws Throwable {

        long start = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        long end = System.currentTimeMillis();
        log.info("Method: {}.{} : Execution Time (ms): {}", joinPoint.getSignature().getDeclaringTypeName(), joinPoint.getSignature().getName(), end - start);
        return result;
    }
}