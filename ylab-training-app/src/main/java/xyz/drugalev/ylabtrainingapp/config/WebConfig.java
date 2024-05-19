package xyz.drugalev.ylabtrainingapp.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import xyz.drugalev.ylabauditspringbootstarter.annotation.EnableAudit;
import xyz.drugalev.ylabtrainingapp.controller.AuthHandlerInterceptor;

/**
 * Web configuration class.
 *
 * @author Drugalev Maxim
 */
@Configuration
@RequiredArgsConstructor
@EnableAspectJAutoProxy
@EnableAudit
public class WebConfig implements WebMvcConfigurer {
    private final AuthHandlerInterceptor authHandlerInterceptor;

    /**
     * Adds the AuthHandlerInterceptor to the InterceptorRegistry.
     *
     * @param registry the InterceptorRegistry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authHandlerInterceptor)
                .excludePathPatterns("/auth/**", "/error", "/swagger-ui/**", "/v3/api-docs/**");
    }
}
