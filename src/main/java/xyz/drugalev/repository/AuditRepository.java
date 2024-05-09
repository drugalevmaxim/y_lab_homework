package xyz.drugalev.repository;

import xyz.drugalev.entity.Audit;
import xyz.drugalev.entity.User;

import java.util.List;

/**
 * Interface for working with the audit table in the database.
 */
public interface AuditRepository {
    /**
     * Saves a new audit record to the database.
     *
     * @param user   the user who performed the action
     * @param action the action that was performed
     */
    void save(User user, String action);

    /**
     * Finds all audit records from the database.
     *
     * @return a list of all audit records
     */
    List<Audit> findAll();
}