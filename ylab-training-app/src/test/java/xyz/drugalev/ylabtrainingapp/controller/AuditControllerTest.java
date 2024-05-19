package xyz.drugalev.ylabtrainingapp.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import xyz.drugalev.entity.Role;
import xyz.drugalev.entity.User;
import xyz.drugalev.ylabtrainingapp.TestYlabTrainingAppApplicationConfiguration;
import xyz.drugalev.ylabtrainingapp.repository.UserRepository;
import xyz.drugalev.ylabtrainingapp.service.JwtService;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Import(TestYlabTrainingAppApplicationConfiguration.class)
public class AuditControllerTest {
    @Autowired
    JwtService jwtService;
    @Autowired
    private MockMvc mvc;
    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("Given admin, when get audit, then status ok")
    public void givenAdmin_whenGetAudit_thenStatusOk() throws Exception {
        User admin = userRepository.save(new User(0, "audit_admin", "audit_admin", new Role(2, "admin")));
        String token = jwtService.getJwtToken(admin).getToken();

        mvc.perform(get("/trainings").header("Authorization", "Bearer " + token));
        mvc.perform(get("/training-types").header("Authorization", "Bearer " + token));
        mvc.perform(get("/audit").header("Authorization", "Bearer " + token));

        mvc.perform(get("/audit").header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("{\"user\":\"audit_admin\",\"action\":\"getUserTrainings\"")))
                .andExpect(content().string(containsString("{\"user\":\"audit_admin\",\"action\":\"getTrainingTypes\"")))
                .andExpect(content().string(containsString("{\"user\":\"audit_admin\",\"action\":\"getAudits\"")));
    }

    @Test
    @DisplayName("Given user, when get audit, then status forbidden")
    public void givenUser_whenGetAudit_thenStatusForbidden() throws Exception {
        User user = userRepository.save(new User(0, "audit_user", "audit_user", new Role(1, "user")));
        String token = jwtService.getJwtToken(user).getToken();
        mvc.perform(get("/audit").header("Authorization", "Bearer " + token))
                .andExpect(status().isForbidden());
    }
}
