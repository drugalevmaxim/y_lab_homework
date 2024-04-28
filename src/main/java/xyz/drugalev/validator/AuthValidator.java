package xyz.drugalev.validator;

public class AuthValidator {
    public static boolean isValid(String login, String password) {
        if (login == null || password == null) {
            return false;
        }
        return (login.length() >= 8 && login.length() <= 255) && (password.length() >= 8 && password.length() <= 255);
    }
}
