package xyz.drugalev.service.impl;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import xyz.drugalev.service.JwtService;
import xyz.drugalev.util.JwtToken;
import xyz.drugalev.dto.UserDto;
import xyz.drugalev.entity.User;
import xyz.drugalev.exception.UserAlreadyExistsException;
import xyz.drugalev.exception.UserDoesNotExistsException;
import xyz.drugalev.mapper.UserMapper;
import xyz.drugalev.repository.UserRepository;
import xyz.drugalev.service.AuthService;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final UserMapper userMapper;

    @Override
    public JwtToken login(UserDto user) throws UserDoesNotExistsException {
        Optional<User> optionalUser = userRepository.findByName(user.getUsername());
        if (optionalUser.isEmpty() || !optionalUser.get().getPassword().equals(user.getPassword())) {
            throw new UserDoesNotExistsException("User with given credentials not found");
        }
        return jwtService.getJwtToken(optionalUser.get());
    }

    @Override
    public JwtToken register(@Valid UserDto user) throws UserAlreadyExistsException {
        try {
            return jwtService.getJwtToken(userRepository.save(userMapper.userDtoToUser(user)));
        } catch (DuplicateKeyException e) {
            throw new UserAlreadyExistsException("User \"%s\" already exists".formatted(user.getUsername()));
        }
    }
}
