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
import xyz.drugalev.ylabtrainingapp.repository.UserRepository;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Import(TestYlabTrainingAppApplicationConfiguration.class)
public class AuthControllerTest {
    @Autowired
    private MockMvc mvc;
    @Autowired
    private UserRepository repository;

    @Test
    @DisplayName("Given existing user, when logging in, then status ok")
    public void givenExistingUser_whenLoggingIn_thenStatusOk() throws Exception {
        repository.save(new User(0, "user", "password", new Role(2, "admin")));

        mvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"user\", \"password\":\"password\"}"))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.token").hasJsonPath());
    }

    @Test
    @DisplayName("Given non existing user, when logging in, then status not found")
    public void givenNonExistingUser_whenLoggingIn_thenStatusNotFound() throws Exception {
        mvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"not_exists\", \"password\":\"not_exists\"}"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Given wrong password, when logging in, then status unauthorized")
    public void givenWrongPassword_whenLoggingIn_thenStatusUnauthorized() throws Exception {
        repository.save(new User(0, "wrong_password", "correct_password", new Role(2, "admin")));

        mvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"wrong_password\", \"password\":\"wrong_password\"}"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Give invalid json, when logging in, then status bad request")
    public void giveInvalidJson_whenLoggingIn_thenStatusBadRequest() throws Exception {
        mvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"not_username\":\"username\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Given non existing user, when registering, then status ok")
    public void givenNonExistingUser_whenRegistering_thenStatusOk() throws Exception {
        mvc.perform(post("/auth/registration")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"new_user\", \"password\":\"new_user\"}"))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.token").hasJsonPath());
    }

    @Test
    @DisplayName("Given existing user, when registering, then status conflict")
    public void givenExistingUser_whenRegistering_thenStatusConflict() throws Exception {
        repository.save(new User(0, "existing_user", "existing_user", new Role(1, "user")));

        mvc.perform(post("/auth/registration")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"existing_user\", \"password\":\"existing_user\"}"))
                .andExpect(status().isConflict());
    }

    @Test
    @DisplayName("Give invalid json, when registering, then status bad request")
    public void giveInvalidJson_whenRegistering_thenStatusBadRequest() throws Exception {
        mvc.perform(post("/auth/registration")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"not_username\":\"username\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Give short username, when registering, then status bad request")
    public void giveShortUsername_whenRegistering_thenStatusBadRequest() throws Exception {
        mvc.perform(post("/auth/registration")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"1\", \"password\":\"2\"}"))
                .andExpect(status().isBadRequest());
    }
}