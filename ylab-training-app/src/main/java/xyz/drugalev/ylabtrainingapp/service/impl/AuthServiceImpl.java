package xyz.drugalev.ylabtrainingapp.service.impl;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import xyz.drugalev.entity.Role;
import xyz.drugalev.ylabtrainingapp.dto.JwtToken;
import xyz.drugalev.ylabtrainingapp.dto.UserDto;
import xyz.drugalev.entity.User;
import xyz.drugalev.ylabtrainingapp.exception.UserAlreadyExistsException;
import xyz.drugalev.ylabtrainingapp.exception.UserDoesNotExistException;
import xyz.drugalev.ylabtrainingapp.mapper.UserMapper;
import xyz.drugalev.ylabtrainingapp.repository.UserRepository;
import xyz.drugalev.ylabtrainingapp.service.AuthService;
import xyz.drugalev.ylabtrainingapp.service.JwtService;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final UserMapper userMapper;

    @Override
    public JwtToken login(UserDto user) throws UserDoesNotExistException {
        Optional<User> optionalUser = userRepository.findByUsername(user.getUsername());
        if (optionalUser.isEmpty() || !optionalUser.get().getPassword().equals(user.getPassword())) {
            throw new UserDoesNotExistException("User with given credentials not found");
        }
        return jwtService.getJwtToken(optionalUser.get());
    }

    @Override
    public JwtToken register(@Valid UserDto user) throws UserAlreadyExistsException {
        try {
            User mappedUser = userMapper.userDtoToUser(user);
            mappedUser.setRole(new Role(1, "user"));
            return jwtService.getJwtToken(userRepository.save(mappedUser));
        } catch (DuplicateKeyException e) {
            throw new UserAlreadyExistsException("User \"%s\" already exists".formatted(user.getUsername()));
        }
    }
}
