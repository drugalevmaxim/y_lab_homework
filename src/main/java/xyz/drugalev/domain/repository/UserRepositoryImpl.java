package xyz.drugalev.domain.repository;

import xyz.drugalev.domain.entity.Role;
import xyz.drugalev.domain.entity.User;
import xyz.drugalev.domain.exception.UserAlreadyExistsException;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * User repository implementation
 * Stores all data in memory
 *
 * @author Drugalev Maxim
 */
public class UserRepositoryImpl implements UserRepository {
    private final Set<User> users = new HashSet<>();

    @Override
    public User save(User user) throws UserAlreadyExistsException {
        if (users.stream().anyMatch(u -> u.getUsername().equals(user.getUsername()))) {
            throw new UserAlreadyExistsException(user.getUsername());
        }
        //First added user will be admin
        if (users.isEmpty()) {
            user.getRoles().add(Role.ADMIN);
        }
        users.add(user);
        return user;
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return users.stream().filter(u -> u.getUsername().equals(username)).findFirst();
    }
}
