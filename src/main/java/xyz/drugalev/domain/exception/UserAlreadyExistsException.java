package xyz.drugalev.domain.exception;

public class UserAlreadyExistsException extends Exception {
    /**
     * Default constructor.
     *
     * @param username username on which exception thrown.
     */
    public UserAlreadyExistsException(String username) {
        super("User \"" + username + "\" already exists");
    }
}
