package xyz.drugalev.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

/**
 * User role entity.
 *
 * @author Drugalev Maxim
 */
@Data
@AllArgsConstructor
public class Role {
    private final int id;
    private final String name;
    private final Set<Privilege> privileges = new HashSet<>();

    public boolean hasPermission(String privilege) {
        return this.privileges.stream().anyMatch(p -> p.getName().equals(privilege));
    }
}