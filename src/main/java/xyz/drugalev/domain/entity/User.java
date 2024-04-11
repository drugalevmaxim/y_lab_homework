package xyz.drugalev.domain.entity;

import lombok.Getter;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * User entity.
 *
 * @author Drugalev Maxim
 */
@Getter
public class User {
    private final String username;
    private final String password;
    private final Set<Role> roles;

    /**
     * Default constructor.
     * @param username user name.
     * @param password user password.
     */
    public User(String username, String password) {
        this.username = username;
        this.password = password;
        roles = new HashSet<>(List.of(Role.USER));
    }

    /**
     * Checks if user has given privilege.
     * @param privilege privilege to check.
     * @return true if user has privilege, false otherwise.
     */
    public boolean hasPrivilege(Privilege privilege) {
        return roles.stream().anyMatch(role -> role.hasPermission((privilege)));
    }
}

