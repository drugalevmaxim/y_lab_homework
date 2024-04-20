package xyz.drugalev;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import xyz.drugalev.config.MigrationLoader;
import xyz.drugalev.domain.exception.ValidationException;
import xyz.drugalev.domain.repository.impl.PrivilegeRepositoryImpl;
import xyz.drugalev.domain.repository.impl.RoleRepositoryImpl;
import xyz.drugalev.domain.repository.impl.UserRepositoryImpl;
import xyz.drugalev.domain.service.UserService;
import xyz.drugalev.domain.service.impl.UserServiceImpl;
import xyz.drugalev.domain.validator.UserValidator;
import xyz.drugalev.usecase.user.LoginUserUseCase;
import xyz.drugalev.domain.exception.UserNotFoundException;
import xyz.drugalev.usecase.user.RegisterUserUseCase;

import java.sql.SQLException;

import static xyz.drugalev.config.JDBCConnectionProvider.setConnectionCredentials;

@Testcontainers
public class UserLoginTest {

    @Container
    private static final PostgreSQLContainer<?> container = new PostgreSQLContainer<>("postgres:alpine");

    private static UserService userService;
    @BeforeAll
    public static void setUp() {
        container.start();
        setConnectionCredentials(container.getJdbcUrl(),container.getUsername(), container.getPassword());
        MigrationLoader.migrate();
        userService = new UserServiceImpl(new UserRepositoryImpl(new RoleRepositoryImpl(new PrivilegeRepositoryImpl())), new UserValidator());
    }

    @Test
    public void successfulRegistrationAndLogin() throws Exception {
        LoginUserUseCase useCase = new LoginUserUseCase(userService);
        RegisterUserUseCase registerUserUseCase = new RegisterUserUseCase(userService);
        registerUserUseCase.register("test_1", "test_1");
        Assertions.assertDoesNotThrow(() -> useCase.login("test_1", "test_1"));
    }

    @Test
    public void unsuccessfulLogin() {
        LoginUserUseCase useCase = new LoginUserUseCase(userService);
        Assertions.assertThrows(UserNotFoundException.class,
                () -> useCase.login("wrongName", "wrongName"));
    }
    @Test
    void unsuccessfulRegistration() {
        RegisterUserUseCase registerUserUseCase = new RegisterUserUseCase(userService);
        Assertions.assertThrows(SQLException.class,
                () -> {
                    registerUserUseCase.register("test_2", "test_2");
                    registerUserUseCase.register("test_2", "test_2");
        });
    }

    @Test
    void invalidUserData() {
        RegisterUserUseCase registerUserUseCase = new RegisterUserUseCase(userService);
        Assertions.assertThrows(ValidationException.class, () -> registerUserUseCase.register("1", "1"));
    }


}
