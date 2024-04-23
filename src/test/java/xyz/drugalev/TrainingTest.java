package xyz.drugalev;

import org.junit.jupiter.api.*;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import xyz.drugalev.config.MigrationLoader;
import xyz.drugalev.domain.entity.Training;
import xyz.drugalev.domain.entity.TrainingType;
import xyz.drugalev.domain.entity.User;
import xyz.drugalev.domain.exception.IllegalDateException;
import xyz.drugalev.domain.exception.NegativeArgumentException;
import xyz.drugalev.domain.repository.TrainingTypeRepository;
import xyz.drugalev.domain.repository.UserRepository;
import xyz.drugalev.domain.repository.impl.PrivilegeRepositoryImpl;
import xyz.drugalev.domain.repository.impl.RoleRepositoryImpl;
import xyz.drugalev.domain.repository.impl.TrainingDataRepositoryImpl;
import xyz.drugalev.domain.repository.impl.TrainingRepositoryImpl;
import xyz.drugalev.domain.repository.impl.TrainingTypeRepositoryImpl;
import xyz.drugalev.domain.repository.impl.UserRepositoryImpl;
import xyz.drugalev.domain.service.TrainingService;
import xyz.drugalev.domain.service.TrainingTypeService;
import xyz.drugalev.domain.service.impl.TrainingServiceImpl;
import xyz.drugalev.domain.service.impl.TrainingTypeServiceImpl;
import xyz.drugalev.usecase.training.AddTrainingDataUseCase;
import xyz.drugalev.usecase.training.AddTrainingUseCase;
import xyz.drugalev.usecase.training.DeleteTrainingUseCase;
import xyz.drugalev.usecase.training.FindTrainingUseCase;
import xyz.drugalev.usecase.training.GetTrainingStatsUseCase;
import xyz.drugalev.usecase.training.UpdateTrainingUseCase;
import xyz.drugalev.usecase.trainingtype.AddTrainingTypeUseCase;
import xyz.drugalev.usecase.trainingtype.FindTrainingTypeUseCase;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static xyz.drugalev.config.JDBCConnectionProvider.setConnectionCredentials;

@Testcontainers
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TrainingTest {
    @Container
    private static final PostgreSQLContainer<?> container = new PostgreSQLContainer<>("postgres:alpine");

    private static TrainingService trainingService;
    private static TrainingTypeService trainingTypeService;
    private static User mockUser1;
    private static User mockUser2;

    @BeforeAll
    public static void setUp() throws SQLException {
        container.start();
        setConnectionCredentials(container.getJdbcUrl(), container.getUsername(), container.getPassword());
        MigrationLoader.migrate();
        TrainingTypeRepository trainingTypeRepository = new TrainingTypeRepositoryImpl();
        UserRepository userRepository = new UserRepositoryImpl(new RoleRepositoryImpl(new PrivilegeRepositoryImpl()));
        trainingTypeService = new TrainingTypeServiceImpl(trainingTypeRepository);
        trainingService = new TrainingServiceImpl(new TrainingRepositoryImpl(trainingTypeRepository, new TrainingDataRepositoryImpl(), userRepository));
        userRepository.save("test_1", "test_1");
        userRepository.save("test_2", "test_2");

        Optional<User> mockUser1Optional = userRepository.find("test_1");
        Optional<User> mockUser2Optional = userRepository.find("test_2");

        if (mockUser1Optional.isEmpty() || mockUser2Optional.isEmpty()) {
            Assertions.fail();
        }

        mockUser1 = mockUser1Optional.get();
        mockUser2 = mockUser2Optional.get();
    }

    @Test
    @Order(1)
    @DisplayName("Adding training type")
    public void addTrainingType() throws SQLException {
        String name = "Training";
        AddTrainingTypeUseCase useCase = new AddTrainingTypeUseCase(trainingTypeService);
        useCase.add(name);
        Assertions.assertTrue(trainingTypeService.findAll().stream().anyMatch(tt -> tt.getName().equals(name)));
    }

    @Test
    @Order(2)
    @DisplayName("Adding and getting 10 training types")
    public void getAllTenTrainingTypes() throws SQLException {
        FindTrainingTypeUseCase useCase = new FindTrainingTypeUseCase(trainingTypeService);
        for (int i = 0; i < 10; i++) {
            trainingTypeService.save(String.valueOf(i));
        }
        Assertions.assertEquals(11, useCase.findAll().size());
    }

    @Test
    @Order(3)
    @DisplayName("Adding training")
    public void addTraining() throws Exception {
        AddTrainingUseCase useCase = new AddTrainingUseCase(trainingService);

        Optional<TrainingType> trainingTypeOptional = trainingTypeService.find("Training");
        if (trainingTypeOptional.isEmpty()) {
            Assertions.fail("No training type");
        }

        useCase.add(mockUser1, LocalDate.now(), trainingTypeOptional.get(), 10, 10);
        Assertions.assertTrue(trainingService.find(mockUser1, LocalDate.now(), trainingTypeOptional.get()).isPresent());
    }

    @Test
    @Order(4)
    @DisplayName("Trying to add an existing training")
    public void addExistingTraining() throws Exception {
        AddTrainingUseCase useCase = new AddTrainingUseCase(trainingService);

        Optional<TrainingType> trainingTypeOptional = trainingTypeService.find("Training");
        if (trainingTypeOptional.isEmpty()) {
            Assertions.fail("No training type");
        }

        Assertions.assertThrows(SQLException.class, () ->
                useCase.add(mockUser1, LocalDate.now(), trainingTypeOptional.get(), 10, 10));
    }

    @Test
    @Order(5)
    @DisplayName("Trying to add training with future date")
    public void addTrainingWithFutureData() throws SQLException {
        AddTrainingUseCase useCase = new AddTrainingUseCase(trainingService);

        Optional<TrainingType> trainingTypeOptional = trainingTypeService.find("Training");
        if (trainingTypeOptional.isEmpty()) {
            Assertions.fail("No training type");
        }

        Assertions.assertThrows(IllegalDateException.class, () ->
                useCase.add(mockUser1, LocalDate.now().plusDays(1), trainingTypeOptional.get(), 10, 10));
    }

    @Test
    @Order(6)
    @DisplayName("Trying to add training with negative duration")
    public void addTrainingWithNegativeDuration() throws SQLException {
        AddTrainingUseCase useCase = new AddTrainingUseCase(trainingService);

        Optional<TrainingType> trainingTypeOptional = trainingTypeService.find("Training");
        if (trainingTypeOptional.isEmpty()) {
            Assertions.fail("No training type");
        }

        Assertions.assertThrows(NegativeArgumentException.class, () ->
                useCase.add(mockUser1, LocalDate.now(), trainingTypeOptional.get(), -1, 1));
    }

    @Test
    @Order(7)
    @DisplayName("Trying to add training with negative burned calories")
    public void addTrainingWithNegativeBurnedCalories() throws SQLException {
        AddTrainingUseCase useCase = new AddTrainingUseCase(trainingService);

        Optional<TrainingType> trainingTypeOptional = trainingTypeService.find("Training");
        if (trainingTypeOptional.isEmpty()) {
            Assertions.fail("No training type");
        }

        Assertions.assertThrows(NegativeArgumentException.class, () ->
                useCase.add(mockUser1, LocalDate.now(), trainingTypeOptional.get(), 1, -1));
    }

    @Test
    @Order(8)
    @DisplayName("Checking if added trainings is sorted")
    public void isTrainingsSorted() throws NegativeArgumentException, SQLException, IllegalDateException {
        AddTrainingUseCase useCase = new AddTrainingUseCase(trainingService);

        Optional<TrainingType> trainingTypeOptional = trainingTypeService.find("Training");
        if (trainingTypeOptional.isEmpty()) {
            Assertions.fail("No training type");
        }

        useCase.add(mockUser1, LocalDate.now().minusDays(2), trainingTypeOptional.get(), 0, 0);
        useCase.add(mockUser1, LocalDate.now().minusDays(1), trainingTypeOptional.get(), 0, 0);
        useCase.add(mockUser1, LocalDate.now().minusDays(3), trainingTypeOptional.get(), 0, 0);
        useCase.add(mockUser1, LocalDate.now().minusDays(6), trainingTypeOptional.get(), 0, 0);
        useCase.add(mockUser1, LocalDate.now().minusDays(4), trainingTypeOptional.get(), 0, 0);
        useCase.add(mockUser1, LocalDate.now().minusDays(5), trainingTypeOptional.get(), 0, 0);
        useCase.add(mockUser1, LocalDate.now().minusDays(7), trainingTypeOptional.get(), 0, 0);

        FindTrainingUseCase findUseCase = new FindTrainingUseCase(trainingService);
        List<Training> trainings = findUseCase.findAllByUser(mockUser1);
        Training prevTraining = trainings.get(0);
        for (Training training : trainings) {
            if (training.getDate().isAfter(prevTraining.getDate())) {
                Assertions.fail();
            }
        }
    }

    @Test
    @Order(9)
    @DisplayName("Getting all trainings")
    public void findAllTrainings() throws Exception {
        FindTrainingUseCase useCase = new FindTrainingUseCase(trainingService);

        List<Training> list = useCase.findAll();
        Assertions.assertEquals(8, list.size());

    }

    @Test
    @Order(10)
    @DisplayName("Getting all trainings of user")
    public void findUserTrainings() throws Exception {
        FindTrainingUseCase useCase = new FindTrainingUseCase(trainingService);

        Optional<TrainingType> trainingTypeOptional = trainingTypeService.find("Training");
        if (trainingTypeOptional.isEmpty()) {
            Assertions.fail("No training type");
        }

        for (int i = 0; i < 5; i++) {
            trainingService.save(mockUser2, LocalDate.now().minusDays(i), trainingTypeOptional.get(), 1, 2);
        }
        Assertions.assertEquals(useCase.findAllByUser(mockUser2).size(), 5);
    }

    @Test
    @Order(11)
    @DisplayName("Getting all trainings of user between dates")
    public void findUserTrainingsBetween() throws Exception {
        FindTrainingUseCase useCase = new FindTrainingUseCase(trainingService);

        List<Training> list = useCase.findBetween(mockUser2, LocalDate.now().minusDays(2), LocalDate.now());
        Assertions.assertEquals(3, list.size());
    }

    @Test
    @Order(12)
    @DisplayName("Getting user training stats")
    public void getTrainingsStats() throws Exception {
        GetTrainingStatsUseCase useCase = new GetTrainingStatsUseCase(trainingService);

        Map<String, Integer> stats = useCase.getTrainingsStats(mockUser2, LocalDate.now().minusDays(4), LocalDate.now());

        Assertions.assertEquals(5, stats.get("Duration"));
        Assertions.assertEquals(10, stats.get("Calories"));
    }

    @Test
    @Order(13)
    @DisplayName("Deleting user training")
    public void deleteTraining() throws Exception {
        DeleteTrainingUseCase useCase = new DeleteTrainingUseCase(trainingService);

        Optional<TrainingType> trainingTypeOptional = trainingTypeService.find("Training");
        if (trainingTypeOptional.isEmpty()) {
            Assertions.fail("No training type");
        }

        useCase.delete(trainingService.find(mockUser2, LocalDate.now().minusDays(1), trainingTypeOptional.get()).get());
        Assertions.assertTrue(trainingService.find(mockUser2, LocalDate.now().minusDays(1), trainingTypeOptional.get()).isEmpty());
    }

    @Test
    @Order(14)
    @DisplayName("Updating user training")
    public void successfulUpdateTrainings() throws Exception {
        UpdateTrainingUseCase useCase = new UpdateTrainingUseCase(trainingService);
        GetTrainingStatsUseCase getStatsUseCase = new GetTrainingStatsUseCase(trainingService);

        Optional<TrainingType> trainingTypeOptional = trainingTypeService.find("Training");
        if (trainingTypeOptional.isEmpty()) {
            Assertions.fail("No training type");
        }

        Optional<Training> trainingOptional = trainingService.find(mockUser2, LocalDate.now(), trainingTypeOptional.get());
        if (trainingOptional.isEmpty()) {
            Assertions.fail("No training");
        }

        useCase.update(trainingOptional.get(), 100, 100);

        Map<String, Integer> stats = getStatsUseCase.getTrainingsStats(mockUser2, LocalDate.now(), LocalDate.now());
        Assertions.assertEquals(100, stats.get("Duration"));
        Assertions.assertEquals(100, stats.get("Calories"));
    }

    @Test
    @Order(15)
    @DisplayName("Updating user training with invalid stats")
    public void unsuccessfulUpdateTrainings() throws SQLException {
        UpdateTrainingUseCase useCase = new UpdateTrainingUseCase(trainingService);

        Optional<TrainingType> trainingTypeOptional = trainingTypeService.find("Training");
        if (trainingTypeOptional.isEmpty()) {
            Assertions.fail("No training type");
        }

        Optional<Training> trainingOptional = trainingService.find(mockUser2, LocalDate.now(), trainingTypeOptional.get());
        if (trainingOptional.isEmpty()) {
            Assertions.fail("No training");
        }

        Assertions.assertThrows(NegativeArgumentException.class, () ->
                useCase.update(trainingOptional.get(), -100, -100)
        );
    }

    @Test
    @Order(16)
    @DisplayName("Adding data to training")
    public void addDataToTraining() throws SQLException {
        AddTrainingDataUseCase useCase = new AddTrainingDataUseCase(trainingService);

        Optional<TrainingType> trainingTypeOptional = trainingTypeService.find("Training");
        if (trainingTypeOptional.isEmpty()) {
            Assertions.fail("No training type");
        }

        Optional<Training> trainingOptional = trainingService.find(mockUser2, LocalDate.now(), trainingTypeOptional.get());
        if (trainingOptional.isEmpty()) {
            Assertions.fail("No training");
        }

        useCase.addTrainingData(trainingOptional.get(), "test", 1);

        Assertions.assertEquals(1, trainingOptional.get().getTrainingData().size());
    }

}

