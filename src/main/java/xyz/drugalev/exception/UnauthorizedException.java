package xyz.drugalev.exception;

/**
 * Exception that is thrown when the user is not authorized.
 */
public class UnauthorizedException extends Exception {
    public UnauthorizedException(String message) {
        super(message);
    }
}