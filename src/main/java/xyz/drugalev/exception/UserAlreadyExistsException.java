package xyz.drugalev.exception;

/**
 * Exception that is thrown when a user with the given username already exists.
 */
public class UserAlreadyExistsException extends Exception {
    public UserAlreadyExistsException(String message) {
        super(message);
    }
}