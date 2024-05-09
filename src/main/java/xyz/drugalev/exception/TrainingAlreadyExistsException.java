package xyz.drugalev.exception;

/**
 * Exception that is thrown when a training with the same name already exists.
 */
public class TrainingAlreadyExistsException extends Exception {
    public TrainingAlreadyExistsException(String message) {
        super(message);
    }
}