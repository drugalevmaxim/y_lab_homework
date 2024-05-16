package xyz.drugalev.in.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import xyz.drugalev.exception.UnauthorizedException;
import xyz.drugalev.service.JwtService;

/**
 * Interceptor that checks if the user is authorized.
 */
@Component
@RequiredArgsConstructor
public class AuthHandlerInterceptor implements HandlerInterceptor {
    private final JwtService jwtService;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (request.getHeader("Authorization") == null || !request.getHeader("Authorization").startsWith("Bearer ")) {
            throw new UnauthorizedException("No authorization credentials provided");
        }
        String token = request.getHeader("Authorization").substring(7);
        request.setAttribute("user", jwtService.getUser(token));
        return true;
    }
}