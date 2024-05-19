package xyz.drugalev.ylabtrainingapp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * User already exists exception.
 *
 * @author Drugalev Maxim
 */
@ResponseStatus(HttpStatus.CONFLICT)
public class UserAlreadyExistsException extends Exception {
    public UserAlreadyExistsException(String message) {
        super(message);
    }
}