package xyz.drugalev.domain.service;

import xyz.drugalev.domain.entity.User;
import xyz.drugalev.domain.exception.UserAlreadyExistsException;
import xyz.drugalev.domain.exception.ValidationException;
import xyz.drugalev.domain.repository.UserRepository;
import xyz.drugalev.domain.validator.UserValidator;

import java.util.Optional;

public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserValidator userValidator;

    public UserServiceImpl(UserRepository userRepository, UserValidator userValidator) {
        this.userRepository = userRepository;
        this.userValidator = userValidator;
    }

    @Override
    public User save(User user) throws ValidationException, UserAlreadyExistsException {
        if (!userValidator.isValidName(user.getUsername()) || !userValidator.isValidPassword(user.getPassword())) {
            throw new ValidationException("Invalid user data: " + userValidator.getDescription());
        }
        return userRepository.save(user);
    }

    @Override
    public Optional<User> findUser(String username) {
        return userRepository.findByUsername(username);
    }
}