package xyz.drugalev.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class LoggingAspect {

    @Pointcut("@annotation(xyz.drugalev.aspect.annotation.Loggable)")
    private void annotatedWithLoggable() {
    }

    @Around("annotatedWithLoggable()")
    public Object logExecution(ProceedingJoinPoint joinPoint) throws Throwable {

        long start = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        long end = System.currentTimeMillis();

        System.out.println("Method: " + joinPoint.getSignature().getName());
        System.out.println("Execution Time (ms): " + (end - start));

        return result;
    }
}