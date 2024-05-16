package xyz.drugalev.ylabtrainingapp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Training does not exist exception.
 *
 * @author Drugalev Maxim
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class TrainingDoesNotExistException extends Exception {
    public TrainingDoesNotExistException(String message) {
        super(message);
    }
}