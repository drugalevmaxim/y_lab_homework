package xyz.drugalev.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * Aspect that logs the execution time of methods.
 */
@Aspect
@Component
public class LoggingAspect {

    /**
     * Pointcut that matches any method in package, excluding the controllers package (somehow if you include one it breaks app).
     */
    @Pointcut("within(xyz.drugalev..*) && !within(xyz.drugalev.in.controller..*)")
    private void anyMethods() {
    }

    /**
     * Advice that logs the execution time of methods matched by the {@link #anyMethods()} pointcut.
     *
     * @param joinPoint the join point
     * @return the result of the method execution
     * @throws Throwable if the method execution throws an exception
     */
    @Around("anyMethods()")
    public Object logExecution(ProceedingJoinPoint joinPoint) throws Throwable {

        long start = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        long end = System.currentTimeMillis();

        System.out.println("Method: " + joinPoint.getSignature().getName());
        System.out.println("Execution Time (ms): " + (end - start));

        return result;
    }
}