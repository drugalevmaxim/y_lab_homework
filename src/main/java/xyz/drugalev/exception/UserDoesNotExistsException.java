package xyz.drugalev.exception;

/**
 * Exception that is thrown when a user with the given username does not exist.
 */
public class UserDoesNotExistsException extends Exception {
    public UserDoesNotExistsException(String message) {
        super(message);
    }
}