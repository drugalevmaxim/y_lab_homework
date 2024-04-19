package xyz.drugalev.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * User entity.
 *
 * @author Drugalev Maxim
 */
@Data
@AllArgsConstructor
public class User {
    private final String username;
    private final String password;
    private final Set<Role> roles = new HashSet<>(List.of(Role.USER));

    /**
     * Checks if user has given privilege.
     *
     * @param privilege privilege to check.
     * @return true if user has privilege, false otherwise.
     */
    public boolean hasPrivilege(Privilege privilege) {
        return roles.stream().anyMatch(role -> role.hasPermission((privilege)));
    }
}

