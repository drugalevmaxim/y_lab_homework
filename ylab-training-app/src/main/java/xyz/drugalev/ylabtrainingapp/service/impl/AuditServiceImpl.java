package xyz.drugalev.ylabtrainingapp.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import xyz.drugalev.ylablogspringbootstarter.aspect.Loggable;
import xyz.drugalev.ylabtrainingapp.dto.AuditDto;
import xyz.drugalev.entity.User;
import xyz.drugalev.ylabtrainingapp.exception.UserPrivilegeException;
import xyz.drugalev.ylabtrainingapp.mapper.AuditMapper;
import xyz.drugalev.repository.AuditRepository;
import xyz.drugalev.ylabtrainingapp.service.AuditService;

import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
@Service
public class AuditServiceImpl implements AuditService {
    private final AuditRepository auditRepository;
    private final AuditMapper auditMapper;

    @Override
    @Loggable
    public List<AuditDto> findAll(User user) throws UserPrivilegeException {
        if (!Objects.equals(user.getRole().getName(), "admin")) {
            throw new UserPrivilegeException("User does not have privilege to see audit log");
        }
        return auditMapper.toAuditDtos(auditRepository.findAll());
    }
}
