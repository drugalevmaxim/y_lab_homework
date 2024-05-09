package xyz.drugalev.exception;

/**
 * Exception that is thrown when a user does not have the required privilege.
 */
public class UserPrivilegeException extends Exception{
    public UserPrivilegeException(String message) {
        super(message);
    }
}