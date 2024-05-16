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
import xyz.drugalev.ylabtrainingapp.entity.Training;
import xyz.drugalev.ylabtrainingapp.entity.TrainingType;
import xyz.drugalev.ylabtrainingapp.repository.TrainingRepository;
import xyz.drugalev.ylabtrainingapp.repository.TrainingTypeRepository;
import xyz.drugalev.ylabtrainingapp.repository.UserRepository;
import xyz.drugalev.ylabtrainingapp.service.JwtService;

import java.time.LocalDate;
import java.util.HashSet;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest()
@Import(TestYlabTrainingAppApplicationConfiguration.class)
@AutoConfigureMockMvc
class TrainingControllerTest {
    private final String jsonPattern = """
            {
                "date": "%s",
                "type": {
                    "id": %d
                },
                "trainingData": [
                    {
                    "name": "test_data_1",
                    "value": 100
                    },
                                       {
                    "name": "test_data_2",
                    "value": 100
                    }
                ],
                "duration": 50,
                "burnedCalories": 60
            }
            """;
    @Autowired
    private MockMvc mvc;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TrainingRepository trainingRepository;
    @Autowired
    private TrainingTypeRepository trainingTypeRepository;
    @Autowired
    private JwtService jwtService;

    @Test
    @DisplayName("Given new training, when save training, then return ok")
    public void givenNewTraining_whenSaveTraining_thenReturnOk() throws Exception {
        TrainingType trainingType = trainingTypeRepository.save(new TrainingType(0, "new_training_type"));
        User user = userRepository.save(new User(0, "new_training_user", "new_training_user", new Role(1, "user")));

        String json = jsonPattern.formatted(LocalDate.now(), trainingType.getId());

        mvc.perform(post("/trainings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer %s".formatted(jwtService.getJwtToken(user).getToken()))
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").hasJsonPath());
    }

    @Test
    @DisplayName("Given existing training, when save training, then return conflict")
    public void givenExistingTraining_whenSaveTraining_thenReturnConflict() throws Exception {
        TrainingType trainingType = trainingTypeRepository.save(new TrainingType(0, "existing_training_type"));
        User user = userRepository.save(new User(0, "existing_training_user", "existing_training_user", new Role(1, "user")));

        String json = jsonPattern.formatted(LocalDate.now(), trainingType.getId());

        trainingRepository.save(new Training(0
                , user
                , LocalDate.now()
                , trainingType
                , 50
                , 60,
                new HashSet<>()));

        mvc.perform(post("/trainings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer %s".formatted(jwtService.getJwtToken(user).getToken()))
                        .content(json))
                .andExpect(status().isConflict());
    }

    @Test
    @DisplayName("Given invalid json, when save training, then return bad request")
    public void givenInvalidJson_whenSaveTraining_thenReturnBadRequest() throws Exception {
        User user = userRepository.save(new User(0, "invalid_json_training_user", "invalid_json_training_user", new Role(1, "user")));
        mvc.perform(post("/trainings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer %s".formatted(jwtService.getJwtToken(user).getToken()))
                        .content("{\"invalid_json\": \"yes\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Given no authorization, when save training, then return unauthorized")
    public void givenNoAuthorization_whenSaveTraining_thenReturnUnauthorized() throws Exception {
        mvc.perform(post("/trainings"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Given user, when getting trainings, then return training list")
    public void givenUser_whenGettingTrainings_thenReturnTrainingList() throws Exception {
        TrainingType trainingType = trainingTypeRepository.save(new TrainingType(0, "get_training_training_type"));
        User user = userRepository.save(new User(0, "get_trainings_user", "get_trainings_user", new Role(1, "user")));
        trainingRepository.save(new Training(0
                , user
                , LocalDate.now()
                , trainingType
                , 50
                , 60,
                new HashSet<>()));
        trainingRepository.save(new Training(0
                , user
                , LocalDate.now().minusDays(1)
                , trainingType
                , 50
                , 60,
                new HashSet<>()));

        mvc.perform(get("/trainings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer %s".formatted(jwtService.getJwtToken(user).getToken())))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()", is(2)));
    }

    @Test
    @DisplayName("Given other user, when getting trainings, then return empty list")
    public void givenOtherUser_whenGettingTrainings_thenReturnEmptyList() throws Exception {
        TrainingType trainingType = trainingTypeRepository.save(new TrainingType(0, "get_others_training_training_type"));
        User user = userRepository.save(new User(0, "get_others_training_user", "get_others_training_user", new Role(1, "user")));
        User otherUser = userRepository.save(new User(0, "get_others_training_other_user", "get_others_training_other_user", new Role(1, "user")));
        trainingRepository.save(new Training(0
                , user
                , LocalDate.now()
                , trainingType
                , 50
                , 60,
                new HashSet<>()));
        trainingRepository.save(new Training(0
                , user
                , LocalDate.now().minusDays(1)
                , trainingType
                , 50
                , 60,
                new HashSet<>()));

        mvc.perform(get("/trainings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer %s".formatted(jwtService.getJwtToken(otherUser).getToken())))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()", is(0)));
    }

    @Test
    @DisplayName("Given time period, when getting trainings, then return training list")
    public void givenTimePeriod_whenGettingTrainings_thenReturnTrainingList() throws Exception {
        TrainingType trainingType = trainingTypeRepository.save(new TrainingType(0, "get_in_period_training_type"));
        TrainingType secondTrainingType = trainingTypeRepository.save(new TrainingType(0, "get_in_period_training_type2"));
        User user = userRepository.save(new User(0, "get_in_period_training_user", "get_in_period_training_user", new Role(1, "user")));
        LocalDate date = LocalDate.now();
        Training trainingToSave = new Training(0
                , user
                , LocalDate.now()
                , trainingType
                , 50
                , 60,
                new HashSet<>());
        trainingRepository.save(trainingToSave);
        trainingToSave.setDate(date.minusDays(1));
        trainingRepository.save(trainingToSave);
        trainingToSave.setType(secondTrainingType);
        trainingRepository.save(trainingToSave);
        trainingToSave.setDate(date.minusDays(2));
        trainingRepository.save(trainingToSave);
        mvc.perform(get("/trainings?startDate=%s&endDate=%s".formatted(date.minusDays(1), date))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer %s".formatted(jwtService.getJwtToken(user).getToken())))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()", is(3)));
    }

    @Test
    @DisplayName("Given admin, when getting all trainings, then return training list")
    public void givenAdmin_whenGettingAllTrainings_thenReturnTrainingList() throws Exception {
        User admin = userRepository.save(new User(0, "get_all_trainings_admin", "get_all_trainings_admin", new Role(2, "admin")));
        mvc.perform(get("/trainings/all")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer %s".formatted(jwtService.getJwtToken(admin).getToken())))
                .andExpect(status().isOk());
    }


    @Test
    @DisplayName("Given user, when getting all trainings, then return forbidden")
    public void givenUser_whenGettingAllTrainings_thenReturnForbidden() throws Exception {
        User user = userRepository.save(new User(0, "get_all_trainings_user", "get_all_trainings_user", new Role(1, "user")));
        mvc.perform(get("/trainings/all")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer %s".formatted(jwtService.getJwtToken(user).getToken())))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Given training id, when getting training, then return training")
    public void givenTrainingId_whenGettingTraining_thenReturnTraining() throws Exception {
        TrainingType trainingType = trainingTypeRepository.save(new TrainingType(0, "id_training_type"));
        User user = userRepository.save(new User(0, "id_training_user", "id_training_user", new Role(1, "user")));
        Training t = trainingRepository.save(new Training(0
                , user
                , LocalDate.now()
                , trainingType
                , 50
                , 60,
                new HashSet<>()));
        mvc.perform(get("/trainings/%d".formatted(t.getId()))
                        .header("Authorization", "Bearer %s".formatted(jwtService.getJwtToken(user).getToken())))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is((int) t.getId())));
    }

    @Test
    @DisplayName("Given non existing training id, when getting training, then return training")
    public void givenNonExistingTrainingId_whenGettingTraining_thenReturnTraining() throws Exception {
        TrainingType trainingType = trainingTypeRepository.save(new TrainingType(0, "bad_id_training_type"));
        User user = userRepository.save(new User(0, "bad_id_training_user", "bad_id_training_user", new Role(1, "user")));
        Training t = trainingRepository.save(new Training(0
                , user
                , LocalDate.now()
                , trainingType
                , 1
                , 1,
                new HashSet<>()));
        mvc.perform(get("/trainings/%d".formatted(t.getId() + 1))
                        .header("Authorization", "Bearer %s".formatted(jwtService.getJwtToken(user).getToken())))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Given training, when updating training, then return updater training")
    public void givenTraining_whenUpdatingTraining_thenReturnUpdaterTraining() throws Exception {
        TrainingType trainingType = trainingTypeRepository.save(new TrainingType(0, "updating_training_type"));
        User user = userRepository.save(new User(0, "updating_training_user", "updating_training_user", new Role(1, "user")));
        Training t = trainingRepository.save(new Training(0
                , user
                , LocalDate.now()
                , trainingType
                , 1
                , 1,
                new HashSet<>()));
        String json = jsonPattern.formatted(t.getDate().minusDays(1), trainingType.getId());
        mvc.perform(put("/trainings/%d".formatted(t.getId()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer %s".formatted(jwtService.getJwtToken(user).getToken()))
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.date", is(t.getDate().minusDays(1).toString())))
                .andExpect(jsonPath("$.duration", is(50)))
                .andExpect(jsonPath("$.burnedCalories", is(60)));
    }

    @Test
    @DisplayName("Given id, when deleting training, then return ok")
    public void givenId_whenDeletingTraining_thenReturnOk() throws Exception {
        TrainingType trainingType = trainingTypeRepository.save(new TrainingType(0, "deleting_training_type"));
        User user = userRepository.save(new User(0, "deleting_training_user", "deleting_training_user", new Role(1, "user")));
        Training t = trainingRepository.save(new Training(0
                , user
                , LocalDate.now()
                , trainingType
                , 1
                , 1,
                new HashSet<>()));
        mvc.perform(delete("/trainings/%d".formatted(t.getId()))
                        .header("Authorization", "Bearer %s".formatted(jwtService.getJwtToken(user).getToken())))
                .andExpect(status().isOk());
    }
}