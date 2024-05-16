package xyz.drugalev.ylabtrainingapp.service;

import xyz.drugalev.ylabtrainingapp.dto.AuditDto;
import xyz.drugalev.entity.User;
import xyz.drugalev.ylabtrainingapp.exception.UserPrivilegeException;

import java.util.List;

/**
 * Audit service.
 *
 * @author Drugalev Maxim
 */
public interface AuditService {
    /**
     * Find all audits for user.
     *
     * @param user current user
     * @return list of audit DTOs
     * @throws UserPrivilegeException if user has no privilege to view audits
     */
    List<AuditDto> findAll(User user) throws UserPrivilegeException;
}