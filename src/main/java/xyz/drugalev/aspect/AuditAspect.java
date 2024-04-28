package xyz.drugalev.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import xyz.drugalev.aspect.annotation.Auditable;
import xyz.drugalev.entity.User;
import xyz.drugalev.repository.impl.AuditRepositoryImpl;
import xyz.drugalev.repository.impl.UserRepositoryImpl;

@Aspect
public class AuditAspect {
    private final AuditRepositoryImpl auditRepository;

    public AuditAspect() {
        this.auditRepository = new AuditRepositoryImpl(new UserRepositoryImpl());
    }

    @Pointcut("@annotation(xyz.drugalev.aspect.annotation.Auditable)")
    private void annotatedWithAuditable() {
    }

    @Around("annotatedWithAuditable()")
    public Object logExecution(ProceedingJoinPoint joinPoint) throws Throwable {

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        for (Object arg : joinPoint.getArgs()) {
            if (arg instanceof User user) {
                auditRepository.save(user, signature.getMethod().getAnnotation(Auditable.class).action());
                break;
            }
        }
        return joinPoint.proceed();
    }
}
