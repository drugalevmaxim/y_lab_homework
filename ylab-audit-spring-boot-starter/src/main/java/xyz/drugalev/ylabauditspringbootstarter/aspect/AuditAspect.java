package xyz.drugalev.ylabauditspringbootstarter.aspect;

import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import xyz.drugalev.entity.Audit;
import xyz.drugalev.entity.User;
import xyz.drugalev.repository.AuditRepository;

import java.time.LocalDateTime;

/**
 * Aspect for logging user actions.
 *
 * @author Drugalev Maxim
 */
@Aspect
@Component
@RequiredArgsConstructor
public class AuditAspect {

    private final AuditRepository auditRepository;

    /**
     * Pointcut for methods annotated with {@link org.springframework.web.bind.annotation.RestController}.
     */
    @Pointcut("@within(org.springframework.web.bind.annotation.RestController) && execution(public * *(..))")
    private void annotatedWithAuditable() {
    }

    /**
     * Logs user actions.
     *
     * @param joinPoint the join point
     * @return the result of the method execution
     * @throws Throwable if an error occurs
     */
    @Around("annotatedWithAuditable()")
    public Object logExecution(ProceedingJoinPoint joinPoint) throws Throwable {

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Object result = joinPoint.proceed();

        for (Object arg : joinPoint.getArgs()) {
            if (arg instanceof User user) {
                auditRepository.save(new Audit(user, signature.getName(), LocalDateTime.now()));
                break;
            }
        }

        return result;
    }
}