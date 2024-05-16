package xyz.drugalev.mapper;

import org.mapstruct.Mapper;
import xyz.drugalev.dto.UserDto;
import xyz.drugalev.entity.User;

/**
 * Mapper for converting between {@link User} and {@link UserDto}.
 */
@Mapper(componentModel = "spring")
public interface UserMapper {

    /**
     * Converts a single {@link UserDto} to a {@link User} entity.
     *
     * @param userDto The {@link UserDto} to convert.
     * @return The converted {@link User} entity.
     */
    User userDtoToUser(UserDto userDto);

    /**
     * Converts a single {@link User} entity to a {@link UserDto}.
     *
     * @param user The {@link User} entity to convert.
     * @return The converted {@link UserDto}.
     */
    UserDto userToUserDto(User user);
}