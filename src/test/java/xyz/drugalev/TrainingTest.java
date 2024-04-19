package xyz.drugalev;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import xyz.drugalev.domain.entity.Training;
import xyz.drugalev.domain.entity.TrainingType;
import xyz.drugalev.domain.entity.User;
import xyz.drugalev.domain.exception.IllegalDatePeriodException;
import xyz.drugalev.domain.exception.NegativeArgumentException;
import xyz.drugalev.domain.exception.TrainingAlreadyExistsException;
import xyz.drugalev.domain.repository.impl.TrainingRepositoryImpl;
import xyz.drugalev.domain.service.TrainingService;
import xyz.drugalev.domain.service.impl.TrainingServiceImpl;
import xyz.drugalev.usecase.training.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class TrainingTest {

    TrainingService trainingService;

    @BeforeEach
    public void setUp() {
        trainingService = new TrainingServiceImpl(new TrainingRepositoryImpl());
    }

    @Test
    public void addTraining() throws Exception {
        AddTrainingUseCase useCase = new AddTrainingUseCase(trainingService);
        Training t = new Training(new User("", ""),
                LocalDate.now(),
                new TrainingType("Test"),
                1, 1);
        useCase.add(t.getPerformer(), t.getDate(), t.getTrainingType(), t.getDuration(), t.getBurnedCalories());
        Assertions.assertTrue(trainingService.find(t.getPerformer(), t.getTrainingType(), t.getDate()).isPresent());
    }

    @Test
    public void addExistingTraining() throws Exception {
        AddTrainingUseCase useCase = new AddTrainingUseCase(trainingService);
        Training t = new Training(new User("", ""),
                LocalDate.now(),
                new TrainingType("Test"),
                1, 1);
        useCase.add(t.getPerformer(), t.getDate(), t.getTrainingType(), t.getDuration(), t.getBurnedCalories());
        Assertions.assertThrows(TrainingAlreadyExistsException.class, () ->
                useCase.add(t.getPerformer(), t.getDate(), t.getTrainingType(), t.getDuration(), t.getBurnedCalories()));
    }

    @Test
    public void addTrainingWithFutureData() {
        AddTrainingUseCase useCase = new AddTrainingUseCase(trainingService);
        Training t = new Training(new User("", ""),
                LocalDate.now().plusDays(1),
                new TrainingType("Test"),
                1, 1);
        Assertions.assertThrows(IllegalDatePeriodException.class, () ->
                useCase.add(t.getPerformer(), t.getDate(), t.getTrainingType(), t.getDuration(), t.getBurnedCalories()));
    }

    @Test
    public void addTrainingWithNegativeDuration() {
        AddTrainingUseCase useCase = new AddTrainingUseCase(trainingService);
        Assertions.assertThrows(NegativeArgumentException.class, () ->
                useCase.add(new User("", ""), LocalDate.now(), new TrainingType(""), -1, 1));
    }

    @Test
    public void addTrainingWithNegativeBurnedCalories() {
        AddTrainingUseCase useCase = new AddTrainingUseCase(trainingService);
        Assertions.assertThrows(NegativeArgumentException.class, () ->
                useCase.add(new User("", ""), LocalDate.now(), new TrainingType(""), 1, -1));
    }

    @Test
    public void isTrainingsSorted() throws TrainingAlreadyExistsException, IllegalDatePeriodException, NegativeArgumentException {
        AddTrainingUseCase useCase = new AddTrainingUseCase(trainingService);
        User u = new User("1", "1");
        TrainingType tt = new TrainingType("Test");

        useCase.add(u, LocalDate.now().minusDays(2), tt, 0, 0);
        useCase.add(u, LocalDate.now(), tt, 0, 0);
        useCase.add(u, LocalDate.now().minusDays(1), tt, 0, 0);
        useCase.add(u, LocalDate.now().minusDays(3), tt, 0, 0);
        useCase.add(u, LocalDate.now().minusDays(6), tt, 0, 0);
        useCase.add(u, LocalDate.now().minusDays(4), tt, 0, 0);
        useCase.add(u, LocalDate.now().minusDays(5), tt, 0, 0);
        useCase.add(u, LocalDate.now().minusDays(7), tt, 0, 0);
        FindTrainingUseCase findUseCase = new FindTrainingUseCase(trainingService);
        List<Training> trainings = findUseCase.findAllByUser(u);
        Training prevTraining = trainings.get(0);
        for (Training training : trainings) {
            if (training.getDate().isAfter(prevTraining.getDate())) {
                Assertions.fail();
            }
        }

    }

    @Test
    public void findAllTrainings() throws Exception {
        FindTrainingUseCase useCase = new FindTrainingUseCase(trainingService);

        for (int i = 0; i < 5; i++) {
            trainingService.save(new Training(new User(String.valueOf(i), String.valueOf(i)), LocalDate.now(), new TrainingType(String.valueOf(i)), 1, 1));
            trainingService.save(new Training(new User(String.valueOf(i) + 5, String.valueOf(i) + 5), LocalDate.now(), new TrainingType(String.valueOf(i)), 1, 1));
        }

        List<Training> list = useCase.findAll();
        Assertions.assertEquals(10, list.size());

    }

    @Test
    public void findUserTrainings() throws Exception {
        FindTrainingUseCase useCase = new FindTrainingUseCase(trainingService);

        User u1 = new User("1", "1");
        User u2 = new User("2", "2");

        for (int i = 0; i < 5; i++) {
            trainingService.save(new Training(u1, LocalDate.now(), new TrainingType(String.valueOf(i)), 1, 1));
            trainingService.save(new Training(u2, LocalDate.now(), new TrainingType(String.valueOf(i)), 1, 1));
        }
        List<Training> list = useCase.findAllByUser(u1);
        Assertions.assertEquals(5, list.size());
        for (Training t : list) {
            Assertions.assertEquals(t.getPerformer(), u1);
        }
    }

    @Test
    public void findUserTrainingsBetween() throws Exception {
        FindTrainingUseCase useCase = new FindTrainingUseCase(trainingService);

        User u1 = new User("1", "1");
        User u2 = new User("2", "2");

        for (int i = 0; i < 5; i++) {
            trainingService.save(new Training(u1, LocalDate.now().minusDays(i), new TrainingType("Test1"), 1, 1));
            trainingService.save(new Training(u2, LocalDate.now().minusDays(i), new TrainingType("Test2"), 1, 1));
        }
        List<Training> list = useCase.findBetween(u1, LocalDate.now().minusDays(2), LocalDate.now());
        Assertions.assertEquals(3, list.size());
    }

    @Test
    public void illegalDatePeriodOnFindUserTrainingsBetween() {
        FindTrainingUseCase useCase = new FindTrainingUseCase(trainingService);

        User u1 = new User("", "");

        Assertions.assertThrows(IllegalDatePeriodException.class, () -> useCase.findBetween(u1, LocalDate.now(), LocalDate.now().minusDays(2)));
    }

    @Test
    public void deleteTraining() throws Exception {
        DeleteTrainingUseCase useCase = new DeleteTrainingUseCase(trainingService);
        Training toDelete = new Training(new User("", ""), LocalDate.now(), new TrainingType("Test1"), 1, 1);
        Training notToDelete = new Training(new User("", ""), LocalDate.now(), new TrainingType("Test2"), 1, 1);
        trainingService.save(toDelete);
        trainingService.save(notToDelete);
        useCase.delete(toDelete);
        Assertions.assertTrue(!trainingService.findAll().contains(toDelete) && trainingService.findAll().contains(notToDelete));
    }

    @Test
    public void getTrainingsStats() throws Exception {
        GetTrainingStatsUseCase useCase = new GetTrainingStatsUseCase(trainingService);

        User u1 = new User("1", "1");
        User u2 = new User("2", "2");

        for (int i = 0; i < 5; i++) {
            trainingService.save(new Training(u1, LocalDate.now().minusDays(i), new TrainingType("Test1"), 5, 1));
            trainingService.save(new Training(u2, LocalDate.now().minusDays(i), new TrainingType("Test2"), 1, 5));
        }

        Map<String, Integer> stats1 = useCase.getTrainingsStats(u1, LocalDate.now().minusDays(4), LocalDate.now());
        Map<String, Integer> stats2 = useCase.getTrainingsStats(u2, LocalDate.now().minusDays(4), LocalDate.now());

        Assertions.assertEquals(25, stats1.get("Duration"));
        Assertions.assertEquals(25, stats2.get("Calories"));

    }
    @Test
    public void successfulUpdateTrainings() throws Exception {
        UpdateTrainingUseCase useCase = new UpdateTrainingUseCase(trainingService);

        User u = new User("", "");
        Training t = new Training(u, LocalDate.now(), new TrainingType("Test1"), 1, 1);
        useCase.update(t, 5, 5);

        Assertions.assertEquals(5, t.getDuration(), t.getBurnedCalories());
    }
    @Test
    public void unsuccessfulUpdateTrainings() {
        UpdateTrainingUseCase useCase = new UpdateTrainingUseCase(trainingService);

        User u = new User("", "");
        Training t = new Training(u, LocalDate.now(), new TrainingType("Test2"), 1, 1);
        Assertions.assertThrows(NegativeArgumentException.class, () ->
                useCase.update(t, 5, -5));
    }



}
