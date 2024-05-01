package xyz.drugalev.mapper;

import org.mapstruct.Mapper;
import xyz.drugalev.dto.UserDto;
import xyz.drugalev.entity.Role;
import xyz.drugalev.entity.User;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper
public interface UserMapper {
    /**
     * Converts a single {@link User} entity to a {@link UserDto}.
     *
     * @param user The {@link User} entity to convert.
     * @return The converted {@link UserDto}.
     */
    UserDto toUserDto(User user);
    /**
     * Custom mapping method to convert a set of {@link Role} entities to a set of strings.
     *
     * @param value The set of {@link Role} entities to convert.
     * @return The converted set of strings.
     */
    default Set<String> map(Set<Role> value) {
        return value.stream().map(Role::getName).collect(Collectors.toSet());
    }

}
