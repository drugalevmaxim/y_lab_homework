package xyz.drugalev.domain.exception;

/**
 * Exception thrown when given int expected to be positive, but negative is given.
 */
public class TrainingAlreadyExistsException extends Exception {
    /**
     * Default constructor.
     */
    public TrainingAlreadyExistsException() {
        super("Training already exists");
    }
}
