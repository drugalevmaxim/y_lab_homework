package xyz.drugalev.repository;

import xyz.drugalev.entity.Audit;
import xyz.drugalev.entity.User;

import java.sql.SQLException;
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
     * @throws SQLException if there is an error saving the record to the database
     */
    void save(User user, String action) throws SQLException;

    /**
     * Finds all audit records from the database.
     *
     * @return a list of all audit records
     * @throws SQLException if there is an error retrieving the records from the database
     */
    List<Audit> findAll() throws SQLException;
}