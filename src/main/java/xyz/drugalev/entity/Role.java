package xyz.drugalev.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

/**
 * Role entity.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Role {
    private long id;
    private String name;
    private Set<Privilege> privileges = new HashSet<>();

    public boolean hasPermission(String privilege) {
        return this.privileges.stream().anyMatch(p -> p.getName().equals(privilege));
    }
}