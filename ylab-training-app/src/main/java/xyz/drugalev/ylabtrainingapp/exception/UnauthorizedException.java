package xyz.drugalev.ylabtrainingapp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Unauthorized exception.
 *
 * @author Drugalev Maxim
 */
@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class UnauthorizedException extends Exception {
    public UnauthorizedException(String message) {
        super(message);
    }
}