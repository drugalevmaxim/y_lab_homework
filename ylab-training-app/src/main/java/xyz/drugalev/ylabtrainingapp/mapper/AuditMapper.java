package xyz.drugalev.ylabtrainingapp.mapper;

import org.mapstruct.Mapper;
import xyz.drugalev.entity.Audit;
import xyz.drugalev.entity.User;
import xyz.drugalev.ylabtrainingapp.dto.AuditDto;

import java.util.List;

/**
 * Audit mapper.
 *
 * @author Drugalev Maxim
 */
@Mapper(componentModel = "spring")
public interface AuditMapper {
    AuditDto toAuditDto(Audit audit);

    List<AuditDto> toAuditDtos(List<Audit> audits);

    default String map(User value) {
        return value.getUsername();
    }
}