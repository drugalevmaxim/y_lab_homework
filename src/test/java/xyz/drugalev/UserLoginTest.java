package xyz.drugalev;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import xyz.drugalev.domain.entity.User;
import xyz.drugalev.domain.repository.impl.UserRepositoryImpl;
import xyz.drugalev.domain.service.UserService;
import xyz.drugalev.domain.service.impl.UserServiceImpl;
import xyz.drugalev.domain.validator.UserValidator;
import xyz.drugalev.usecase.user.LoginUserUseCase;
import xyz.drugalev.domain.exception.UserNotFoundException;

public class UserLoginTest {
    private final User mockUser = new User("123456", "123456");
    private UserService userService;

    @BeforeEach
    public void setUp() throws Exception {
        userService = new UserServiceImpl(new UserRepositoryImpl(), new UserValidator());
        userService.save(mockUser);
    }

    @Test
    public void successfulLogin() throws Exception {
        LoginUserUseCase useCase = new LoginUserUseCase(userService);
        Assertions.assertEquals(mockUser, useCase.login(mockUser.getUsername(), mockUser.getPassword()));
    }

    @Test
    public void unsuccessfulLogin() {
        LoginUserUseCase useCase = new LoginUserUseCase(userService);
        Assertions.assertThrows(UserNotFoundException.class,
                () -> useCase.login(mockUser.getUsername() + "wrongName", mockUser.getPassword()));
    }
}
