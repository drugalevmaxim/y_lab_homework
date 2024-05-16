package xyz.drugalev.ylabtrainingapp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Training type already exists exception.
 *
 * @author Drugalev Maxim
 */
@ResponseStatus(HttpStatus.CONFLICT)
public class TrainingTypeAlreadyExistsException extends Exception {
    public TrainingTypeAlreadyExistsException(String message) {
        super(message);
    }
}