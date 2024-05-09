package xyz.drugalev.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

/**
 * User entity.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private long id;
    private String username;
    private String password;
    private final Set<Role> roles = new HashSet<>();

    /**
     * Checks if user has given privilege.
     *
     * @param privilege privilege to check.
     * @return true if user has privilege, false otherwise.
     */
    public boolean hasPrivilege(String privilege) {
        return roles.stream().anyMatch(role -> role.hasPermission((privilege)));
    }
}

