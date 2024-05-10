package xyz.drugalev.in.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
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
import xyz.drugalev.dto.TrainingTypeDto;
import xyz.drugalev.entity.Privilege;
import xyz.drugalev.entity.Role;
import xyz.drugalev.entity.TrainingType;
import xyz.drugalev.entity.User;
import xyz.drugalev.mapper.TrainingTypeMapper;
import xyz.drugalev.repository.TrainingTypeRepository;
import xyz.drugalev.service.impl.TrainingTypeServiceImpl;

import java.util.List;
import java.util.Set;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringJUnitWebConfig(WebConfig.class)
@ExtendWith(MockitoExtension.class)
class TrainingTypeControllerTest {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final TrainingType trainingType = new TrainingType(1, "Training Type");
    private final TrainingTypeDto trainingTypeDto = new TrainingTypeDto(1, "Training Type");
    private final User user = new User(1, "admin", "admin", Set.of(
            new Role(1, "ROLE_ADMIN", Set.of(
                    new Privilege(1, "ADD_TRAINING_TYPE")))));
    private MockMvc mockMvc;
    @InjectMocks
    private TrainingTypeServiceImpl trainingTypeService;
    @Mock
    private TrainingTypeRepository trainingTypeRepository;
    @Mock
    private TrainingTypeMapper trainingTypeMapper;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new TrainingTypeController(trainingTypeService))
                .setControllerAdvice(new ExceptionApiHandler())
                .build();
    }

    @Test
    @SneakyThrows
    void getAll() {
        when(trainingTypeService.getAll()).thenReturn(List.of(trainingTypeDto));
        MvcResult mvcResult = mockMvc.perform(get("/training-types").requestAttr("user", user))
                .andExpect(status().isOk())
                .andReturn();
        String result = mvcResult.getResponse().getContentAsString();
        Assertions.assertEquals(objectMapper.writeValueAsString(List.of(trainingTypeDto)), result);
    }

    @Test
    @SneakyThrows
    void create() {
        when(trainingTypeService.save(user, trainingTypeDto)).thenReturn(trainingTypeDto);
        MvcResult mvcResult = mockMvc.perform(post("/training-types").requestAttr("user", user)
                        .content(objectMapper.writeValueAsString(trainingTypeDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        String result = mvcResult.getResponse().getContentAsString();
        Assertions.assertEquals(objectMapper.writeValueAsString(trainingTypeDto), result);
    }
}