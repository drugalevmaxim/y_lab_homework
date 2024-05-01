package xyz.drugalev.validator;

/**
 * Validator for authentication credentials.
 */
public class AuthValidator {

    /**
     * Validates authentication credentials.
     *
     * @param login    The login to validate.
     * @param password The password to validate.
     * @return True if the credentials are valid, false otherwise.
     */
    public static boolean isValid(String login, String password) {
        if (login == null || password == null) {
            return false;
        }
        return (login.length() >= 8 && login.length() <= 255) && (password.length() >= 8 && password.length() <= 255);
    }

}