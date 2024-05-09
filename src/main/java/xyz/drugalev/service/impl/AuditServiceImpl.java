package xyz.drugalev.service.impl;

import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import xyz.drugalev.dto.AuditDto;
import xyz.drugalev.entity.User;
import xyz.drugalev.exception.AccessDeniedException;
import xyz.drugalev.mapper.AuditMapper;
import xyz.drugalev.repository.AuditRepository;
import xyz.drugalev.service.AuditService;

import java.sql.SQLException;
import java.util.List;

@RequiredArgsConstructor
public class AuditServiceImpl implements AuditService {
    private final AuditRepository auditRepository;
    private final AuditMapper auditMapper = Mappers.getMapper(AuditMapper.class);

    @Override
    public List<AuditDto> findAll(User user) throws SQLException, AccessDeniedException {
        if (!user.hasPrivilege("SEE_AUDIT_LOG")) {
            throw new AccessDeniedException();
        }
        return auditMapper.toAuditDtos(auditRepository.findAll());
    }
}
