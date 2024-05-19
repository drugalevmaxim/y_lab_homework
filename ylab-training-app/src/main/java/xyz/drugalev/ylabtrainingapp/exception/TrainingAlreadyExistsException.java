package xyz.drugalev.ylabtrainingapp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Training already exists exception.
 *
 * @author Drugalev Maxim
 */
@ResponseStatus(HttpStatus.CONFLICT)
public class TrainingAlreadyExistsException extends Exception {
    public TrainingAlreadyExistsException(String message) {
        super(message);
    }
}