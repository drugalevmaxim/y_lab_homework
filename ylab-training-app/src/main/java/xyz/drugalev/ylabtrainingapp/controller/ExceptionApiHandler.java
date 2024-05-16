package xyz.drugalev.ylabtrainingapp.controller;

import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.net.URI;

/**
 * Exception API handler.
 *
 * @author Drugalev Maxim
 */
@RestControllerAdvice
public class ExceptionApiHandler {

    /**
     * Handles JWT decode and verification exceptions.
     *
     * @param ex      the exception
     * @param request the web request
     * @return a problem detail
     */
    @ExceptionHandler({ JWTDecodeException.class, JWTVerificationException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ProblemDetail handleJwtException(Exception ex, WebRequest request) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
        problemDetail.setInstance(URI.create(request.getContextPath()));
        return problemDetail;
    }

    /**
     * Handles data integrity violation exceptions.
     *
     * @param ex      the exception
     * @param request the web request
     * @return a problem detail
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ProblemDetail handleIntegrityViolation(Exception ex, WebRequest request) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "Insufficient or invalid data provided. Additional information: " + ex.getMessage());
        problemDetail.setInstance(URI.create(request.getContextPath()));
        return problemDetail;
    }

    /**
     * Handles token expired exceptions.
     *
     * @param ex      the exception
     * @param request the web request
     * @return a problem detail
     */
    @ExceptionHandler(TokenExpiredException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ProblemDetail handleUnauthorized(Exception ex, WebRequest request) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED, ex.getMessage());
        problemDetail.setInstance(URI.create(request.getContextPath()));
        return problemDetail;
    }
}