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
import xyz.drugalev.domain.repository.impl.*;
import xyz.drugalev.domain.service.TrainingService;
import xyz.drugalev.domain.service.TrainingTypeService;
import xyz.drugalev.domain.service.impl.TrainingServiceImpl;
import xyz.drugalev.domain.service.impl.TrainingTypeServiceImpl;
import xyz.drugalev.usecase.training.*;
import xyz.drugalev.usecase.trainingtype.AddTrainingTypeUseCase;
import xyz.drugalev.usecase.trainingtype.FindTrainingTypeUseCase;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static xyz.drugalev.config.JDBCConnectionProvider.setConnectionCredentials;

@Testcontainers
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TrainingTest {
    @Container
    private static final PostgreSQLContainer<?> container = new PostgreSQLContainer<>("postgres:alpine");

    private static UserRepository userRepository;
    private static TrainingService trainingService;
    private static TrainingTypeService trainingTypeService;
    private static User mockUser1;
    private static User mockUser2;
    @BeforeAll
    public static void setUp() throws SQLException {
        container.start();
        setConnectionCredentials(container.getJdbcUrl(),container.getUsername(), container.getPassword());
        MigrationLoader.migrate();
        TrainingTypeRepository trainingTypeRepository = new TrainingTypeRepositoryImpl();
        userRepository = new UserRepositoryImpl(new RoleRepositoryImpl(new PrivilegeRepositoryImpl()));
        trainingTypeService = new TrainingTypeServiceImpl(trainingTypeRepository);
        trainingService = new TrainingServiceImpl(new TrainingRepositoryImpl(trainingTypeRepository, new TrainingDataRepositoryImpl(), userRepository));
        userRepository.save("test_1", "test_1");
        userRepository.save("test_2", "test_2");
        mockUser1 = userRepository.find("test_1").get();
        mockUser2 = userRepository.find("test_2").get();
    }

    @Test
    @Order(1)
    public void addTrainingType() throws SQLException {
        String name = "Training";
        AddTrainingTypeUseCase useCase = new AddTrainingTypeUseCase(trainingTypeService);
        useCase.add(name);
        Assertions.assertTrue(trainingTypeService.findAll().stream().anyMatch(tt -> tt.getName().equals(name)));
    }

    @Test
    @Order(2)
    public void getAllTenTrainingTypes() throws SQLException {
        FindTrainingTypeUseCase useCase = new FindTrainingTypeUseCase(trainingTypeService);
        for (int i = 0; i < 10; i++) {
            trainingTypeService.save(String.valueOf(i));
        }
        Assertions.assertEquals(11, useCase.findAll().size());
    }

    @Test
    @Order(3)
    public void addTraining() throws Exception {
        AddTrainingUseCase useCase = new AddTrainingUseCase(trainingService);
        useCase.add(mockUser1, LocalDate.now(), trainingTypeService.find("Training").get(), 10, 10);
        Assertions.assertTrue(trainingService.find(mockUser1, LocalDate.now(),  trainingTypeService.find("Training").get()).isPresent());
    }

    @Test
    @Order(4)
    public void addExistingTraining() throws Exception {
        AddTrainingUseCase useCase = new AddTrainingUseCase(trainingService);
       Assertions.assertThrows(SQLException.class, () ->
                useCase.add(mockUser1, LocalDate.now(), trainingTypeService.find("Training").get(), 10, 10));
    }

    @Test
    @Order(5)
    public void addTrainingWithFutureData() {
        AddTrainingUseCase useCase = new AddTrainingUseCase(trainingService);
        Assertions.assertThrows(IllegalDateException.class, () ->
                useCase.add(mockUser1, LocalDate.now().plusDays(1), trainingTypeService.find("Training").get(), 10, 10));
    }

    @Test
    @Order(6)
    public void addTrainingWithNegativeDuration() {
        AddTrainingUseCase useCase = new AddTrainingUseCase(trainingService);
        Assertions.assertThrows(NegativeArgumentException.class, () ->
                useCase.add(mockUser1, LocalDate.now(), trainingTypeService.find("Training").get(), -1, 1));
    }

    @Test
    @Order(7)
    public void addTrainingWithNegativeBurnedCalories() {
        AddTrainingUseCase useCase = new AddTrainingUseCase(trainingService);
        Assertions.assertThrows(NegativeArgumentException.class, () ->
                useCase.add(mockUser1, LocalDate.now(), trainingTypeService.find("Training").get(), 1, -1));
    }

    @Test
    @Order(8)
    public void isTrainingsSorted() throws NegativeArgumentException, SQLException, IllegalDateException {
        AddTrainingUseCase useCase = new AddTrainingUseCase(trainingService);
        TrainingType tt = trainingTypeService.find("Training").get();

        useCase.add(mockUser1, LocalDate.now().minusDays(2), tt, 0, 0);
        useCase.add(mockUser1, LocalDate.now().minusDays(1), tt, 0, 0);
        useCase.add(mockUser1, LocalDate.now().minusDays(3), tt, 0, 0);
        useCase.add(mockUser1, LocalDate.now().minusDays(6), tt, 0, 0);
        useCase.add(mockUser1, LocalDate.now().minusDays(4), tt, 0, 0);
        useCase.add(mockUser1, LocalDate.now().minusDays(5), tt, 0, 0);
        useCase.add(mockUser1, LocalDate.now().minusDays(7), tt, 0, 0);

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
    public void findAllTrainings() throws Exception {
        FindTrainingUseCase useCase = new FindTrainingUseCase(trainingService);

        List<Training> list = useCase.findAll();
        Assertions.assertEquals(8, list.size());

    }

    @Test
    @Order(10)
    public void findUserTrainings() throws Exception {
        FindTrainingUseCase useCase = new FindTrainingUseCase(trainingService);
        for (int i = 0; i < 5; i++) {
            trainingService.save(mockUser2, LocalDate.now().minusDays(i), trainingTypeService.find("Training").get(), 1, 2);
        }
        Assertions.assertEquals(useCase.findAllByUser(mockUser2).size(), 5);
    }

    @Test
    @Order(11)
    public void findUserTrainingsBetween() throws Exception {
        FindTrainingUseCase useCase = new FindTrainingUseCase(trainingService);

        List<Training> list = useCase.findBetween(mockUser2, LocalDate.now().minusDays(2), LocalDate.now());
        Assertions.assertEquals(3, list.size());
    }

    @Test
    @Order(12)
    public void getTrainingsStats() throws Exception {
        GetTrainingStatsUseCase useCase = new GetTrainingStatsUseCase(trainingService);

        Map<String, Integer> stats = useCase.getTrainingsStats(mockUser2, LocalDate.now().minusDays(4), LocalDate.now());

        Assertions.assertEquals(5, stats.get("Duration"));
        Assertions.assertEquals(10, stats.get("Calories"));
    }

    @Test
    @Order(13)
    public void deleteTraining() throws Exception {
        DeleteTrainingUseCase useCase = new DeleteTrainingUseCase(trainingService);
        useCase.delete(trainingService.find(mockUser2, LocalDate.now().minusDays(1), trainingTypeService.find("Training").get()).get());
        Assertions.assertTrue(trainingService.find(mockUser2, LocalDate.now().minusDays(1), trainingTypeService.find("Training").get()).isEmpty());
    }

    @Test
    @Order(14)
    public void successfulUpdateTrainings() throws Exception {
        UpdateTrainingUseCase useCase = new UpdateTrainingUseCase(trainingService);
        GetTrainingStatsUseCase getStatsUseCase = new GetTrainingStatsUseCase(trainingService);

        Training t = trainingService.find(mockUser2, LocalDate.now(), trainingTypeService.find("Training").get()).get();

        useCase.update(t, 100, 100);

        Map<String, Integer> stats = getStatsUseCase.getTrainingsStats(mockUser2, LocalDate.now(), LocalDate.now());
        Assertions.assertEquals(100, stats.get("Duration"));
        Assertions.assertEquals(100, stats.get("Calories"));
    }

    @Test
    @Order(15)
    public void unsuccessfulUpdateTrainings() throws SQLException {
        UpdateTrainingUseCase useCase = new UpdateTrainingUseCase(trainingService);

        Training t = trainingService.find(mockUser2, LocalDate.now(), trainingTypeService.find("Training").get()).get();

        Assertions.assertThrows(NegativeArgumentException.class, () ->
                useCase.update(t, -100, -100)
        );
    }

    @Test
    @Order(16)
    public void addDataToTraining() throws SQLException {
        AddTrainingDataUseCase useCase = new AddTrainingDataUseCase(trainingService);

        Training t = trainingService.find(mockUser2, LocalDate.now(), trainingTypeService.find("Training").get()).get();

        useCase.addTrainingData(t, "test", 1);

        Assertions.assertEquals(1, trainingService.find(mockUser2, LocalDate.now(), trainingTypeService.find("Training").get()).get().getTrainingData().size());
    }

}

