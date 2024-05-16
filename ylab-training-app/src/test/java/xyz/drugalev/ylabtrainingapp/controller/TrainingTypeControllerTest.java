package xyz.drugalev.ylabtrainingapp.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import xyz.drugalev.entity.Role;
import xyz.drugalev.entity.User;
import xyz.drugalev.ylabtrainingapp.TestYlabTrainingAppApplicationConfiguration;
import xyz.drugalev.ylabtrainingapp.entity.TrainingType;
import xyz.drugalev.ylabtrainingapp.repository.TrainingTypeRepository;
import xyz.drugalev.ylabtrainingapp.repository.UserRepository;
import xyz.drugalev.ylabtrainingapp.service.JwtService;

import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest()
@Import(TestYlabTrainingAppApplicationConfiguration.class)
@AutoConfigureMockMvc
public class TrainingTypeControllerTest {
    @Autowired
    JwtService jwtService;
    @Autowired
    private MockMvc mvc;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TrainingTypeRepository trainingTypeRepository;

    @Test
    @DisplayName("Given new training type, when create, then status ok")
    public void givenNewTrainingType_whenCreate_thenStatusOk() throws Exception {
        User admin = userRepository.save(new User(0, "new_type_user", "new_type_user", new Role(2, "admin")));
        String json = "{\"name\":\"new_type\"}";

        mvc.perform(post("/training-types")
                        .contentType("application/json")
                        .content(json)
                        .header("Authorization", "Bearer " + jwtService.getJwtToken(admin).getToken()))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Given existing training type, when create, then status conflict")
    public void givenExistingTrainingType_whenCreate_thenStatusConflict() throws Exception {
        User admin = userRepository.save(new User(0, "existing_type_user", "existing_type_user", new Role(2, "admin")));
        trainingTypeRepository.save(new TrainingType(0, "existing_type"));
        String json = "{\"name\":\"existing_type\"}";
        mvc.perform(post("/training-types")
                        .contentType("application/json")
                        .content(json)
                        .header("Authorization", "Bearer " + jwtService.getJwtToken(admin).getToken()))
                .andExpect(status().isConflict());
    }

    @Test
    @DisplayName("Given user without admin role, when create, then status forbidden")
    public void givenUserWithoutAdminRole_whenCreate_thenStatusForbidden() throws Exception {
        User user = userRepository.save(new User(0, "non_admin_user", "non_admin_user", new Role(1, "user")));
        String json = "{\"name\":\"non_admin_type\"}";
        mvc.perform(post("/training-types")
                        .contentType("application/json")
                        .content(json)
                        .header("Authorization", "Bearer " + jwtService.getJwtToken(user).getToken()))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Given user, when get, then status ok")
    public void givenUser_whenGet_thenStatusOk() throws Exception {
        User user = userRepository.save(new User(0, "get_user", "get_user", new Role(1, "user")));
        trainingTypeRepository.save(new TrainingType(0, "get_type"));
        mvc.perform(get("/training-types")
                        .header("Authorization", "Bearer " + jwtService.getJwtToken(user).getToken()))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()", greaterThanOrEqualTo(1)));
    }
}