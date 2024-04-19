package xyz.drugalev.domain.repository.impl;

import lombok.NonNull;
import xyz.drugalev.domain.entity.Role;
import xyz.drugalev.domain.entity.User;
import xyz.drugalev.domain.exception.UserAlreadyExistsException;
import xyz.drugalev.domain.repository.UserRepository;

import java.util.Map;
import java.util.HashMap;
import java.util.Optional;

/**
 * User repository implementation
 * Stores all data in memory
 *
 * @author Drugalev Maxim
 */
public class UserRepositoryImpl implements UserRepository {
    private final Map<String, User> users = new HashMap<>();

    @Override
    public User save(@NonNull User user) throws UserAlreadyExistsException {
        if (users.containsKey(user.getUsername())) {
            throw new UserAlreadyExistsException(user.getUsername());
        }
        if (users.isEmpty()) {
            user.getRoles().add(Role.ADMIN);
        }
        users.put(user.getUsername(), user);
        return user;
    }

    @Override
    public Optional<User> findByUsername(@NonNull String username) {
        return users.containsKey(username) ? Optional.of(users.get(username)) : Optional.empty();
    }
}
