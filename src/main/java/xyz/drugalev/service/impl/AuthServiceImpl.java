package xyz.drugalev.service.impl;

import lombok.RequiredArgsConstructor;
import xyz.drugalev.entity.User;
import xyz.drugalev.exception.InvalidParametersException;
import xyz.drugalev.exception.UserAlreadyExistsException;
import xyz.drugalev.exception.UserNotFoundException;
import xyz.drugalev.repository.UserRepository;
import xyz.drugalev.service.AuthService;
import xyz.drugalev.validator.AuthValidator;

import java.sql.SQLException;
import java.util.Optional;

@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    @Override
    public User login(String username, String password) throws SQLException, UserNotFoundException {
        Optional<User> user = userRepository.find(username);
        if (user.isEmpty() || !(user.get().getPassword().equals(password))) {
            throw new UserNotFoundException(username);
        }
        return user.get();
    }

    @Override
    public User register(String username, String password) throws SQLException, UserAlreadyExistsException, InvalidParametersException {
        if (!AuthValidator.isValid(username, password)) {
            throw new InvalidParametersException();
        }
        Optional<User> user = userRepository.find(username);
        if (user.isPresent()) {
            throw new UserAlreadyExistsException(username);
        }
        User newUser = new User();
        newUser.setUsername(username);
        newUser.setPassword(password);
        return userRepository.save(newUser);
    }
}
