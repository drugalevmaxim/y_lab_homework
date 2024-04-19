package xyz.drugalev.domain.exception;

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
