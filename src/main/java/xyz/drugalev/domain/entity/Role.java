package xyz.drugalev.domain.entity;

import java.util.Arrays;
import java.util.List;

/**
 * User roles
 *
 * @author Drugalev Maxim
 */
public enum Role {
    /**
     * Default user privileges
     */
    USER(),
    /**
     * Administrator privileges, able to add training types
     */
    ADMIN(Privilege.ADD_TRAINING_TYPE, Privilege.SEE_OTHERS_TRAININGS, Privilege.SEE_AUDIT_LOG);
    private final List<Privilege> privileges;

    /**
     * Constructor for roles
     *
     * @param privileges privileges that role will have
     * @see Privilege
     */
    Role(Privilege... privileges) {
        this.privileges = Arrays.stream(privileges).toList();
    }

    /**
     * Check if role has permission
     *
     * @param privilege privilege to check
     * @return true if role has privilege, false otherwise
     */
    public boolean hasPermission(Privilege privilege) {
        return this.privileges.contains(privilege);
    }
}