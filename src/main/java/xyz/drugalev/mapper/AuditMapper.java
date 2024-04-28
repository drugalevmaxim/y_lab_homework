package xyz.drugalev.mapper;

import org.mapstruct.Mapper;
import xyz.drugalev.dto.AuditDto;
import xyz.drugalev.entity.Audit;
import xyz.drugalev.entity.User;

import java.util.List;

@Mapper
public interface AuditMapper {
    AuditDto toAuditDto(Audit audit);
    List<AuditDto> toAuditDtos(List<Audit> audits);
    default String map(User value) {
        return value.getUsername();
    }
}
