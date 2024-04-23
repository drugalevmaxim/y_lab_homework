package xyz.drugalev.domain.exception;

/**
 * Exception thrown when given date is incorrect,
 * i.e. start date > today's date.
 *
 * @author Drugalev Maxim
 */
public class IllegalDateException extends Exception {
    /**
     * Default constructor.
     *
     * @param message message of exception.
     */
    public IllegalDateException(String message) {
        super(message);
    }
}
