package xyz.drugalev.in.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
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
import xyz.drugalev.dto.TrainingDto;
import xyz.drugalev.dto.TrainingTypeDto;
import xyz.drugalev.entity.*;
import xyz.drugalev.mapper.TrainingMapper;
import xyz.drugalev.repository.TrainingRepository;
import xyz.drugalev.service.impl.TrainingServiceImpl;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringJUnitWebConfig(WebConfig.class)
@ExtendWith(MockitoExtension.class)
public class TrainingControllerTest {
    private final ObjectMapper objectMapper = JsonMapper.builder()
            .addModule(new JavaTimeModule())
            .build();
    private final User user = new User(1, "admin", "admin", Set.of(
            new Role(1, "ROLE_ADMIN", Set.of(
                    new Privilege(1, "OTHER_USERS_TRAININGS")))));
    private final TrainingType trainingType = new TrainingType(1, "Training Type");
    private final Training training = new Training(1, user, LocalDate.parse("2023-01-01"), trainingType, 10, 10);
    private final TrainingDto trainingDto = new TrainingDto(1, "user", LocalDate.now(), new TrainingTypeDto(1, "Training Type"), 10, 10);
    private MockMvc mockMvc;
    @InjectMocks
    private TrainingServiceImpl trainingServiceImpl;
    @Mock
    private TrainingRepository trainingRepository;
    @Mock
    private TrainingMapper trainingMapper;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new TrainingController(trainingServiceImpl))
                .setControllerAdvice(new ExceptionApiHandler())
                .build();
    }

    @Test
    @SneakyThrows
    public void testGetAllUser() {
        when(trainingServiceImpl.findAllByUser(user)).thenReturn(List.of(trainingDto));
        MvcResult mvcResult = mockMvc.perform(get("/trainings").requestAttr("user", user))
                .andExpect(status().isOk())
                .andReturn();
        String result = mvcResult.getResponse().getContentAsString();
        Assertions.assertEquals(objectMapper.writeValueAsString(List.of(trainingDto)), result);
    }

    @Test
    @SneakyThrows
    public void testGetAll() {
        when(trainingServiceImpl.findAllByUser(user)).thenReturn(List.of(trainingDto));
        MvcResult mvcResult = mockMvc.perform(get("/trainings/all").requestAttr("user", user))
                .andExpect(status().isOk())
                .andReturn();
        String result = mvcResult.getResponse().getContentAsString();
        Assertions.assertEquals(objectMapper.writeValueAsString(List.of(trainingDto)), result);
    }

    @Test
    @SneakyThrows
    public void testGetAllBetweenDates() {
        when(trainingServiceImpl.findByUserBetweenDates(user, LocalDate.parse("2023-01-01"), LocalDate.parse("2023-01-31"))).thenReturn(List.of(trainingDto));
        MvcResult mvcResult = mockMvc.perform(get("/trainings").requestAttr("user", user)
                        .param("startDate", "2023-01-01")
                        .param("endDate", "2023-01-31"))
                .andExpect(status().isOk())
                .andReturn();
        String result = mvcResult.getResponse().getContentAsString();
        Assertions.assertEquals(objectMapper.writeValueAsString(List.of(trainingDto)), result);
    }

    @Test
    @SneakyThrows
    public void testGetSpecified() {
        when(trainingRepository.find(anyLong())).thenReturn(Optional.of(training));
        when(trainingServiceImpl.find(user, anyLong())).thenReturn(trainingDto);
        MvcResult mvcResult = mockMvc.perform(get("/trainings/1").requestAttr("user", user))
                .andExpect(status().isOk())
                .andReturn();
        String result = mvcResult.getResponse().getContentAsString();
        Assertions.assertEquals(objectMapper.writeValueAsString(trainingDto), result);
    }

    @Test
    @SneakyThrows
    public void testCreate() {
        when(trainingMapper.toTraining(any())).thenReturn(training);
        when(trainingServiceImpl.save(user, any(TrainingDto.class))).thenReturn(trainingDto);
        MvcResult mvcResult = mockMvc.perform(post("/trainings")
                        .requestAttr("user", user)
                        .content(objectMapper.writeValueAsString(trainingDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        String result = mvcResult.getResponse().getContentAsString();
        Assertions.assertEquals(objectMapper.writeValueAsString(trainingDto), result);
    }

    @Test
    @SneakyThrows
    public void testUpdate() {
        when(trainingRepository.find(anyLong())).thenReturn(Optional.of(training));
        when(trainingMapper.toTraining(any())).thenReturn(training);
        when(trainingServiceImpl.update(user, anyLong(), trainingDto)).thenReturn(trainingDto);
        MvcResult mvcResult = mockMvc.perform(put("/trainings/1")
                        .requestAttr("user", user)
                        .content(objectMapper.writeValueAsString(trainingDto))
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andReturn();
        String result = mvcResult.getResponse().getContentAsString();
        Assertions.assertEquals(objectMapper.writeValueAsString(trainingDto), result);
    }

    @Test
    @SneakyThrows
    public void testDelete() {
        when(trainingRepository.find(anyLong())).thenReturn(Optional.of(training));
        mockMvc.perform(delete("/trainings/1").requestAttr("user", user))
                .andExpect(status().isOk())
                .andReturn();
    }
}

