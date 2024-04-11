package xyz.drugalev.domain.validator;

/**
 * User validation class.
 *
 * @author Drugalev Maxim
 */
public class UserValidator {
    private static final String DESCRIPTION = "Username and password need to be at least 6 symbols long";

    /**
     * Checks if given name is valid.
     * @param name name to validate.
     * @return true if name is valid, false otherwise.
     */
    public boolean isValidName(String name) {
        return name != null && !name.isBlank() && name.length() > 5;
    }

    /**
     * Checks if given password is valid.
     * @param password password to validate.
     * @return true if password is valid, false otherwise.
     */
    public boolean isValidPassword(String password) {
        return password != null && !password.isBlank() && password.length() > 5;
    }

    /**
     * Get description of validator.
     * @return description of validator.
     */
    public String getDescription() {
        return DESCRIPTION;
    }
}