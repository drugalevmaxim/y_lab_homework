package xyz.drugalev.usecase.training;

import xyz.drugalev.domain.entity.Training;
import xyz.drugalev.domain.entity.User;
import xyz.drugalev.domain.service.TrainingService;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

/**
 * UseCase for finding and getting trainings.
 *
 * @author Drugalev Maxim
 */
public class FindTrainingUseCase {
    private final TrainingService trainingService;

    /**
     * Default constructor.
     *
     * @param trainingService service that use case works with.
     */
    public FindTrainingUseCase(TrainingService trainingService) {
        this.trainingService = trainingService;
    }

    /**
     * Returns list of all trainings.
     *
     * @return list of all trainings.
     */
    public List<Training> findAll() throws SQLException {
        return trainingService.findAll();
    }

    /**
     * Returns list of all users trainings.
     *
     * @param user user who performed training.
     * @return list of all trainings.
     */
    public List<Training> findAllByUser(User user) throws SQLException {
        return trainingService.findAllByUser(user);
    }

    /**
     * Returns list of all users trainings performed between given dates.
     *
     * @param user  user who performed training.
     * @param date1 starting date.
     * @param date2 ending date.
     * @return list of all trainings.
     */
    public List<Training> findBetween(User user, LocalDate date1, LocalDate date2) throws  SQLException {

        return trainingService.findByUserBetweenDates(user, date1, date2);
    }
}
