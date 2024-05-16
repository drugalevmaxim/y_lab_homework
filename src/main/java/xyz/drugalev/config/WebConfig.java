package xyz.drugalev.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import xyz.drugalev.in.controller.AuthHandlerInterceptor;

/**
 * Configuration class for Spring MVC.
 */
@Configuration
@EnableWebMvc
@EnableAspectJAutoProxy
@ComponentScan(basePackages = {"xyz.drugalev"})
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {
    private final AuthHandlerInterceptor authHandlerInterceptor;
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authHandlerInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/auth/**", "/swagger-ui/**", "/v3/api-docs/**");
    }
}