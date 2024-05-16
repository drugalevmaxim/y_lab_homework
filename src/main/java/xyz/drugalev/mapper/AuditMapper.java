package xyz.drugalev.mapper;

import org.mapstruct.Mapper;
import xyz.drugalev.dto.AuditDto;
import xyz.drugalev.entity.Audit;
import xyz.drugalev.entity.User;

import java.util.List;

/**
 * Mapper for converting between {@link Audit} and {@link AuditDto}.
 */
@Mapper(componentModel = "spring")
public interface AuditMapper {

    /**
     * Converts a single {@link Audit} entity to an {@link AuditDto}.
     *
     * @param audit The {@link Audit} entity to convert.
     * @return The converted {@link AuditDto}.
     */
    AuditDto toAuditDto(Audit audit);

    /**
     * Converts a list of {@link Audit} entities to a list of {@link AuditDto}s.
     *
     * @param audits The list of {@link Audit} entities to convert.
     * @return The converted list of {@link AuditDto}s.
     */
    List<AuditDto> toAuditDtos(List<Audit> audits);

    /**
     * Custom mapping method to convert a {@link User} entity to a string.
     *
     * @param value The {@link User} entity to convert.
     * @return The converted string.
     */
    default String map(User value) {
        return value.getUsername();
    }
}