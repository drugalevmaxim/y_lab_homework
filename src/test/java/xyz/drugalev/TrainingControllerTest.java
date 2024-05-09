package xyz.drugalev;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import xyz.drugalev.config.WebConfig;
import xyz.drugalev.in.controller.ExceptionApiHandler;
import xyz.drugalev.in.controller.TrainingController;
import xyz.drugalev.entity.Privilege;
import xyz.drugalev.entity.Role;
import xyz.drugalev.entity.Training;
import xyz.drugalev.entity.TrainingType;
import xyz.drugalev.entity.User;
import xyz.drugalev.mapper.TrainingMapper;
import xyz.drugalev.repository.TrainingRepository;
import xyz.drugalev.service.impl.TrainingServiceImpl;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringJUnitWebConfig(WebConfig.class)
@ExtendWith(MockitoExtension.class)
public class TrainingControllerTest {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final User user = new User(1, "admin", "admin", Set.of(
            new Role(1, "ROLE_ADMIN", Set.of(
                    new Privilege(1, "OTHER_USERS_TRAININGS")))));
    private final TrainingType trainingType = new TrainingType(1, "Training Type");
    private final Training training = new Training(1, user, LocalDate.now(), trainingType, 10, 10);
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
        MvcResult mvcResult = mockMvc.perform(get("/trainings").requestAttr("user", user))
                .andExpect(status().isOk())
                .andReturn();
        String result = mvcResult.getResponse().getContentAsString();
        Assertions.assertNotNull(result);
    }

    @Test
    @SneakyThrows
    public void testGetAll() {
        when(trainingRepository.findAll()).thenReturn(List.of(training));
        MvcResult mvcResult = mockMvc.perform(get("/trainings/all").requestAttr("user", user))
                .andExpect(status().isOk())
                .andReturn();
        String result = mvcResult.getResponse().getContentAsString();
        Assertions.assertNotNull(result);
    }
    // TODO: other tests...
}
