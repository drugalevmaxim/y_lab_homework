package xyz.drugalev.ylabtrainingapp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * User does not exist exception.
 *
 * @author Drugalev Maxim
 */
@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class UserDoesNotExistException extends Exception {
    public UserDoesNotExistException(String message) {
        super(message);
    }
}