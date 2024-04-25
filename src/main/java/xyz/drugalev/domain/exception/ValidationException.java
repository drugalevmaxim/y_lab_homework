package xyz.drugalev.domain.exception;

/**
 * Exception thrown when given data has not passed validation.
 *
 * @author Drugalev Maxim
 */
public class ValidationException extends Exception {
    /**
     * Default constructor.
     *
     * @param message message of exception.
     */
    public ValidationException(String message) {
        super(message);
    }
}
