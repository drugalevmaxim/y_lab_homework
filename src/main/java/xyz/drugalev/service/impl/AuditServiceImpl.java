package xyz.drugalev.service.impl;

import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;
import xyz.drugalev.dto.AuditDto;
import xyz.drugalev.entity.User;
import xyz.drugalev.exception.UserPrivilegeException;
import xyz.drugalev.mapper.AuditMapper;
import xyz.drugalev.repository.AuditRepository;
import xyz.drugalev.service.AuditService;

import java.util.List;

@RequiredArgsConstructor
@Service
public class AuditServiceImpl implements AuditService {
    private final AuditRepository auditRepository;
    private final AuditMapper auditMapper = Mappers.getMapper(AuditMapper.class);

    @Override
    public List<AuditDto> findAll(User user) throws UserPrivilegeException {
        if (!user.hasPrivilege("SEE_AUDIT_LOG")) {
            throw new UserPrivilegeException("User does not have privilege to see audit log");
        }
        return auditMapper.toAuditDtos(auditRepository.findAll());
    }
}
