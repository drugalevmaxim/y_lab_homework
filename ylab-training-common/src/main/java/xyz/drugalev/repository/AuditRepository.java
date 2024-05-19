package xyz.drugalev.repository;

import xyz.drugalev.entity.Audit;

import java.util.List;

/**
 * Audit repository.
 *
 * @author Drugalev Maxim
 */
public interface AuditRepository {
    /**
     * Save audit entity.
     *
     * @param audit audit entity
     * @return saved audit entity
     */
    Audit save(Audit audit);

    /**
     * Find all audit entities.
     *
     * @return list of audit entities
     */
    List<Audit> findAll();
}