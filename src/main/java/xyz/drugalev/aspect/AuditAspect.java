package xyz.drugalev.aspect;

import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import xyz.drugalev.entity.User;
import xyz.drugalev.repository.AuditRepository;

/**
 * Aspect that logs the execution of methods annotated with {@link org.springframework.web.bind.annotation.RestController}.
 * For each execution, it saves the user who made the request to the audit repository.
 */
@Aspect
@Component
@RequiredArgsConstructor
public class AuditAspect {
    private final AuditRepository auditRepository;

    /**
     * Pointcut that matches methods annotated with {@link org.springframework.web.bind.annotation.RestController}.
     */
    @Pointcut("within(@org.springframework.web.bind.annotation.RestController *)")
    private void annotatedWithAuditable() {
    }

    /**
     * Advice that logs the execution of methods annotated with {@link org.springframework.web.bind.annotation.RestController}.
     * For each execution, it saves the user who made the request to the audit repository.
     *
     * @param joinPoint the join point
     * @return the result of the method execution
     * @throws Throwable if the method execution throws an exception
     */
    @Around("annotatedWithAuditable()")
    public Object logExecution(ProceedingJoinPoint joinPoint) throws Throwable {

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Object result = joinPoint.proceed();

        for (Object arg : joinPoint.getArgs()) {
            if (arg instanceof User user) {
                auditRepository.save(user, signature.getMethod().getName());
                break;
            }
        }

        return result;
    }
}