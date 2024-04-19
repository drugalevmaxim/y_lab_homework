package xyz.drugalev;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import xyz.drugalev.domain.exception.UserAlreadyExistsException;
import xyz.drugalev.domain.exception.ValidationException;
import xyz.drugalev.domain.repository.impl.UserRepositoryImpl;
import xyz.drugalev.domain.service.UserService;
import xyz.drugalev.domain.service.impl.UserServiceImpl;
import xyz.drugalev.domain.validator.UserValidator;
import xyz.drugalev.usecase.user.RegisterUserUseCase;

public class UserRegistrationTest {
    private UserService userService;

    @BeforeEach
    public void setUp() {
        userService = new UserServiceImpl(new UserRepositoryImpl(), new UserValidator());
    }

    @Test
    public void successfulRegistration() throws Exception {
        RegisterUserUseCase useCase = new RegisterUserUseCase(userService);
        useCase.register("testUser", "testUser");
        Assertions.assertTrue(userService.findUser("testUser").isPresent());
    }

    @Test
    public void unsuccessfulRegistration() throws Exception {
        RegisterUserUseCase useCase = new RegisterUserUseCase(userService);
        useCase.register("testUser", "testUser1");
        Assertions.assertThrows(UserAlreadyExistsException.class, () ->
                useCase.register("testUser", "testUser2")
        );
    }

    @Test
    public void invalidUserData() {
        RegisterUserUseCase useCase = new RegisterUserUseCase(userService);
        Assertions.assertThrows(ValidationException.class, () ->
                useCase.register("test", "test")
        );
    }
}
