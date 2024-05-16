package xyz.drugalev.exception;

/**
 * Exception that is thrown when a training type with the given name already exists.
 */
public class TrainingTypeAlreadyExistsException extends Exception {
    public TrainingTypeAlreadyExistsException(String message) {
        super(message);
    }
}