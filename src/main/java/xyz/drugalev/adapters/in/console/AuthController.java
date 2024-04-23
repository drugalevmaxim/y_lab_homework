package xyz.drugalev.adapters.in.console;

import xyz.drugalev.adapters.logger.Audit;
import xyz.drugalev.domain.entity.User;
import xyz.drugalev.domain.exception.UserNotFoundException;
import xyz.drugalev.domain.exception.ValidationException;
import xyz.drugalev.domain.service.UserService;
import xyz.drugalev.usecase.user.LoginUserUseCase;
import xyz.drugalev.usecase.user.RegisterUserUseCase;

import java.sql.SQLException;

/**
 * The user auth controller.
 *
 * @author Drugalev Maxim
 */
public class AuthController {

    /**
     * Login screen user.
     *
     * @param userService the user service
     * @param inputUtil   the input util
     * @return the logged-in user
     * @throws UserNotFoundException the user not found exception
     * @throws SQLException          the sql exception
     */
    public static User loginScreen(UserService userService, InputUtil inputUtil)
            throws UserNotFoundException, SQLException {
        System.out.print("Username");
        String username = inputUtil.getLine();
        System.out.print("Password");
        String password = inputUtil.getPassword();

        LoginUserUseCase useCase = new LoginUserUseCase(userService);

            User user = useCase.login(username, password);
            Audit.getInstance().log(user, "logging in");

            return user;
    }

    /**
     * Register screen user.
     *
     * @param userService the user service
     * @param inputUtil   the input util
     * @return the registered user
     * @throws ValidationException the validation exception
     * @throws SQLException        the sql exception
     */
    public static User registerScreen(UserService userService, InputUtil inputUtil)
            throws ValidationException, SQLException {
        System.out.print("Username");
        String username = inputUtil.getLine();
        System.out.print("Password");
        String password = inputUtil.getPassword();

        RegisterUserUseCase useCase = new RegisterUserUseCase(userService);

        User user = useCase.register(username, password);
        Audit.getInstance().log(user, "registration");
        return user;
    }

}
