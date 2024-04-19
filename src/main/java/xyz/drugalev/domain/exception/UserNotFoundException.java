package xyz.drugalev.domain.exception;

public class UserNotFoundException extends Exception {
    /**
     * Default constructor.
     *
     * @param username username on which exception thrown.
     */
    public UserNotFoundException(String username) {
        super("User \"" + username + "\" not found");
    }
}
