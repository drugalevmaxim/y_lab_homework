package xyz.drugalev.in.controller;

import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import xyz.drugalev.exception.TrainingAlreadyExistsException;
import xyz.drugalev.exception.TrainingDoesNotExistsException;
import xyz.drugalev.exception.UnauthorizedException;
import xyz.drugalev.exception.UserAlreadyExistsException;
import xyz.drugalev.exception.UserDoesNotExistsException;
import xyz.drugalev.exception.UserPrivilegeException;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

/**
 * Exception handler for the API.
 */
@RestControllerAdvice
public class ExceptionApiHandler {

    /**
     * Handles the {@link UserAlreadyExistsException}, {@link HttpMessageNotReadableException},
     * {@link JWTDecodeException}, {@link JWTVerificationException}, and {@link TrainingAlreadyExistsException} exceptions.
     *
     * @param ex      the exception
     * @param request the web request
     * @return the problem detail
     */
    @ExceptionHandler({UserAlreadyExistsException.class, HttpMessageNotReadableException.class,
            JWTDecodeException.class, JWTVerificationException.class, TrainingAlreadyExistsException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ProblemDetail handleUserAlreadyExists(Exception ex, WebRequest request) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
        problemDetail.setInstance(URI.create(request.getContextPath()));
        return problemDetail;
    }

    /**
     * Handles the {@link MethodArgumentNotValidException} exception.
     *
     * @param ex      the exception
     * @param request the web request
     * @return the problem detail
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ProblemDetail handleValidation(MethodArgumentNotValidException ex, WebRequest request) {
        List<String> details = new ArrayList<>();
        for (ObjectError error : ex.getBindingResult().getAllErrors()) {
            details.add(error.getDefaultMessage());
        }
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, details.toString());
        problemDetail.setInstance(URI.create(request.getContextPath()));
        return problemDetail;
    }

    /**
     * Handles the {@link DataIntegrityViolationException} exception.
     *
     * @param ex      the exception
     * @param request the web request
     * @return the problem detail
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ProblemDetail handleIntegrityViolation(Exception ex, WebRequest request) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "Insufficient or invalid data provided");
        problemDetail.setInstance(URI.create(request.getContextPath()));
        return problemDetail;
    }

    /**
     * Handles the {@link UnauthorizedException}, {@link UserDoesNotExistsException}, and {@link TokenExpiredException} exceptions.
     *
     * @param ex      the exception
     * @param request the web request
     * @return the problem detail
     */
    @ExceptionHandler({UnauthorizedException.class, UserDoesNotExistsException.class,
            TokenExpiredException.class})
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ProblemDetail handleUnauthorized(Exception ex, WebRequest request) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED, ex.getMessage());
        problemDetail.setInstance(URI.create(request.getContextPath()));
        return problemDetail;
    }

    /**
     * Handles the {@link UserPrivilegeException} exception.
     *
     * @param ex      the exception
     * @param request the web request
     * @return the problem detail
     */
    @ExceptionHandler({UserPrivilegeException.class})
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ProblemDetail handlePrivilege(Exception ex, WebRequest request) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.FORBIDDEN, ex.getMessage());
        problemDetail.setInstance(URI.create(request.getContextPath()));
        return problemDetail;
    }

    /**
     * Handles the {@link TrainingDoesNotExistsException} exception.
     *
     * @param ex      the exception
     * @param request the web request
     * @return the problem detail
     */
    @ExceptionHandler({TrainingDoesNotExistsException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ProblemDetail handleTrainingDoesNotExists(Exception ex, WebRequest request) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
        problemDetail.setInstance(URI.create(request.getContextPath()));
        return problemDetail;
    }
}