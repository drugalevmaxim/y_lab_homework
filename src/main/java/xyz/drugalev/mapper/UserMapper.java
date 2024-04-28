package xyz.drugalev.mapper;

import org.mapstruct.Mapper;
import xyz.drugalev.dto.UserDto;
import xyz.drugalev.entity.Role;
import xyz.drugalev.entity.User;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper
public interface UserMapper {
    UserDto toUserDto(User user);
    List<UserDto> toUserDtos(List<User> users);
    default Set<String> map(Set<Role> value) {
        return value.stream().map(Role::getName).collect(Collectors.toSet());
    }

}
