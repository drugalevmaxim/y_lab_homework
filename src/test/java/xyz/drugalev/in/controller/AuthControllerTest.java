package xyz.drugalev.in.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import xyz.drugalev.config.WebConfig;
import xyz.drugalev.dto.UserDto;
import xyz.drugalev.entity.User;
import xyz.drugalev.mapper.UserMapper;
import xyz.drugalev.repository.UserRepository;
import xyz.drugalev.service.JwtService;
import xyz.drugalev.service.impl.AuthServiceImpl;
import xyz.drugalev.util.JwtToken;

import java.util.HashSet;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringJUnitWebConfig(WebConfig.class)
@ExtendWith(MockitoExtension.class)
public class AuthControllerTest {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final UserDto userCredentials = new UserDto("admin", "admin");
    private final User user = new User(1, "admin", "admin", new HashSet<>());
    private final JwtToken jwtToken = new JwtToken("token");
    private MockMvc mockMvc;
    @InjectMocks
    private AuthServiceImpl authServiceImpl;
    @Mock
    private UserRepository userRepository;
    @Mock
    private JwtService jwtService;
    @Mock
    private UserMapper userMapper;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new AuthController(authServiceImpl))
                .setControllerAdvice(new ExceptionApiHandler())
                .build();
    }

    @Test
    @SneakyThrows
    @DisplayName("Successful registration test")
    void registerTest() {
        when(jwtService.getJwtToken(any())).thenReturn(jwtToken);

        MvcResult mvcResult = mockMvc.perform(post("/auth/registration").content(objectMapper.writeValueAsString(userCredentials))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String result = mvcResult.getResponse().getContentAsString();
        Assertions.assertEquals("{\"token\":\"token\"}", result);
    }

    @Test
    @SneakyThrows
    @DisplayName("Successful login test")
    void loginTest() {
        when(userRepository.findByName(userCredentials.getUsername())).thenReturn(Optional.of(user));
        when(jwtService.getJwtToken(user)).thenReturn(jwtToken);

        MvcResult mvcResult = mockMvc.perform(post("/auth/login")
                        .content(objectMapper.writeValueAsString(userCredentials))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String result = mvcResult.getResponse().getContentAsString();
        Assertions.assertEquals("{\"token\":\"token\"}", result);
    }
}
