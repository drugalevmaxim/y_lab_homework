package xyz.drugalev.domain.exception;

/**
 * Exception thrown when given int expected to be positive, but negative is given.
 */
public class NegativeArgumentException extends Exception {
    /**
     * Default constructor.
     *
     * @param message message of exception.
     */
    public NegativeArgumentException(String message) {
        super(message);
    }
}
