package xyz.drugalev.exception;

/**
 * Exception that is thrown when a training with the given name does not exist.
 */
public class TrainingDoesNotExistsException extends Exception {
    public TrainingDoesNotExistsException(String message) {
        super(message);
    }
}