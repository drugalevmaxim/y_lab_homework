package xyz.drugalev.in.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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
import xyz.drugalev.dto.AuditDto;
import xyz.drugalev.entity.Audit;
import xyz.drugalev.entity.Privilege;
import xyz.drugalev.entity.Role;
import xyz.drugalev.entity.User;
import xyz.drugalev.mapper.AuditMapper;
import xyz.drugalev.repository.AuditRepository;
import xyz.drugalev.service.impl.AuditServiceImpl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringJUnitWebConfig(WebConfig.class)
@ExtendWith(MockitoExtension.class)
class AuditControllerTest {
    private final ObjectMapper objectMapper = JsonMapper.builder()
            .addModule(new JavaTimeModule())
            .build();
    private final User user = new User(1, "admin", "admin", Set.of(
            new Role(1, "ROLE_ADMIN", Set.of(
                    new Privilege(1, "SEE_AUDIT_LOG")))));
    private final Audit audit = new Audit(user, "test", LocalDateTime.now());
    private final AuditDto auditDto = new AuditDto(user.getUsername(), "test", LocalDateTime.now());
    private MockMvc mockMvc;
    @InjectMocks
    private AuditServiceImpl auditServiceImpl;
    @Mock
    private AuditRepository auditRepository;
    @Mock
    private AuditMapper auditMapper;
    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new AuditController(auditServiceImpl))
                .setControllerAdvice(new ExceptionApiHandler())
                .build();
    }

    @Test
    @SneakyThrows
    @DisplayName("Successfully get audit log")
    void audit() {
        when(auditServiceImpl.findAll(user)).thenReturn(List.of(auditDto));
        MvcResult mvcResult = mockMvc.perform(get("/audit").requestAttr("user", user))
                .andExpect(status().isOk())
                .andReturn();
        String result = mvcResult.getResponse().getContentAsString();
        Assertions.assertEquals(objectMapper.writeValueAsString(List.of(auditDto)), result);
    }
}