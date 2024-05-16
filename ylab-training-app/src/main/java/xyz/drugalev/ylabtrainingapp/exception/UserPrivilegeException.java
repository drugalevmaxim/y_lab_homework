package xyz.drugalev.ylabtrainingapp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * User Privilege exception.
 *
 * @author Drugalev Maxim
 */
@ResponseStatus(HttpStatus.FORBIDDEN)
public class UserPrivilegeException extends Exception {
    public UserPrivilegeException(String message) {
        super(message);
    }
}