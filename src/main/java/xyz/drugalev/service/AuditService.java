package xyz.drugalev.service;

import xyz.drugalev.dto.AuditDto;
import xyz.drugalev.entity.User;
import xyz.drugalev.exception.UserPrivilegeException;

import java.util.List;

public interface AuditService {

    /**
     * Finds all audit records.
     *
     * @param user the user which making request
     * @return a list of all audit records
     * @throws UserPrivilegeException if the user does not have permission to view the audit records
     */
    List<AuditDto> findAll(User user) throws UserPrivilegeException;
}