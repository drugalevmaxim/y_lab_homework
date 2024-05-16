package xyz.drugalev.in.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.drugalev.dto.UserDto;
import xyz.drugalev.exception.UserAlreadyExistsException;
import xyz.drugalev.exception.UserDoesNotExistsException;
import xyz.drugalev.util.JwtToken;
import xyz.drugalev.service.AuthService;

@RestController
@SecurityRequirement(name = "bearerAuth")
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    /**
     * Logs in a user.
     *
     * @param user the user
     * @return the JWT token
     * @throws UserDoesNotExistsException if the user does not exist
     */
    @Operation(summary = "Login", description = "Logs in a user.")
    @PostMapping(value = "/login", produces = "application/json", consumes = "application/json")
    public JwtToken login(@RequestBody UserDto user) throws UserDoesNotExistsException {
        return authService.login(user);
    }

    /**
     * Registers a user.
     *
     * @param user the user
     * @return the JWT token
     * @throws UserAlreadyExistsException if the user already exists
     */
    @Operation(summary = "Register", description = "Registers a user.")
    @PostMapping(value = "/registration", produces = "application/json", consumes = "application/json")
    public JwtToken register(@RequestBody @Validated UserDto user) throws UserAlreadyExistsException {
        return authService.register(user);
    }
}