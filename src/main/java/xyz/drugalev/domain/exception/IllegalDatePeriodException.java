package xyz.drugalev.domain.exception;

/**
 * Exception thrown when given date period is incorrect,
 * i.e. start date > end date.
 */
public class IllegalDatePeriodException extends Exception {

    /**
     * Default constructor.
     *
     * @param message message of exception.
     */
    public IllegalDatePeriodException(String message) {
        super(message);
    }
}
