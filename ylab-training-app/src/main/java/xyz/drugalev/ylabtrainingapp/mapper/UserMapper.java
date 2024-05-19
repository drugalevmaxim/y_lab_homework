package xyz.drugalev.ylabtrainingapp.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import xyz.drugalev.entity.User;
import xyz.drugalev.ylabtrainingapp.dto.UserDto;

/**
 * User mapper.
 *
 * @author Drugalev Maxim
 */
@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "id", ignore = true)
    User userDtoToUser(UserDto userDto);
}