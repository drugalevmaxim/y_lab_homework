package xyz.drugalev.ylabtrainingapp.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.drugalev.ylabtrainingapp.dto.JwtToken;
import xyz.drugalev.ylabtrainingapp.dto.UserDto;
import xyz.drugalev.ylabtrainingapp.exception.UserAlreadyExistsException;
import xyz.drugalev.ylabtrainingapp.exception.UserDoesNotExistException;
import xyz.drugalev.ylabtrainingapp.service.AuthService;

/**
 * Authentication controller.
 *
 * @author Drugalev Maxim
 */
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    /**
     * Logs in a user.
     *
     * @param user the user credentials
     * @return a JWT token
     * @throws UserDoesNotExistException if the user does not exist
     */
    @Operation(summary = "Login", description = "Logs in a user.")
    @PostMapping(value = "/login", produces = "application/json", consumes = "application/json")
    public JwtToken login(@RequestBody @Valid UserDto user) throws UserDoesNotExistException {
        return authService.login(user);
    }

    /**
     * Registers a user.
     *
     * @param user the user credentials
     * @return a JWT token
     * @throws UserAlreadyExistsException if the user already exists
     */
    @Operation(summary = "Registration", description = "Registers a user.")
    @PostMapping(path = "/registration", produces = "application/json", consumes = "application/json")
    public JwtToken register(@RequestBody @Valid UserDto user) throws UserAlreadyExistsException {
        return authService.register(user);
    }
}
