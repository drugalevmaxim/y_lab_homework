package xyz.drugalev.ylabtrainingapp.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import xyz.drugalev.ylabtrainingapp.exception.UnauthorizedException;
import xyz.drugalev.ylabtrainingapp.service.JwtService;

/**
 * Authentication handler interceptor.
 *
 * @author Drugalev Maxim
 */
@Component
@RequiredArgsConstructor
public class AuthHandlerInterceptor implements HandlerInterceptor {
    private final JwtService jwtService;

    /**
     * Checks if the request is authorized.
     *
     * @param request  the HTTP request
     * @param response the HTTP response
     * @param handler  the handler
     * @return true if the request is authorized
     * @throws UnauthorizedException if the request is not authorized
     */
    @Override
    public boolean preHandle(HttpServletRequest request,
                             @NonNull HttpServletResponse response,
                             @NonNull Object handler) throws UnauthorizedException {
        if (request.getHeader("Authorization") == null || !request.getHeader("Authorization").startsWith("Bearer ")) {
            throw new UnauthorizedException("No authorization credentials provided");
        }
        String token = request.getHeader("Authorization").substring(7);
        request.setAttribute("user", jwtService.getUser(token));
        return true;
    }
}