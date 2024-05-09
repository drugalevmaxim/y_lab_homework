package xyz.drugalev.exception;

/**
 * Exception thrown when a user is not found.
 */
public class UserNotFoundException extends Exception {
    public UserNotFoundException() {}
    public UserNotFoundException(String message) {
        super(message);
    }
}
