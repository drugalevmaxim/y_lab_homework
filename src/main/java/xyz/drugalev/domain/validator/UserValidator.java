package xyz.drugalev.domain.validator;

/**
 * User validation class.
 *
 * @author Drugalev Maxim
 */
public class UserValidator {
    private static final String DESCRIPTION = "Username and password length should be >=6 and <64 ";

    /**
     * Checks if given name is valid.
     *
     * @param name name to validate.
     * @return true if name is valid, false otherwise.
     */
    public boolean isValidName(String name) {
        return name != null && !name.isBlank() && name.length() >= 6 && name.length() < 64;
    }

    /**
     * Checks if given password is valid.
     *
     * @param password password to validate.
     * @return true if password is valid, false otherwise.
     */
    public boolean isValidPassword(String password) {
        return password != null && !password.isBlank() && password.length() >= 6 && password.length() < 64;
    }

    /**
     * Get description of validator.
     *
     * @return description of validator.
     */
    public String getDescription() {
        return DESCRIPTION;
    }
}