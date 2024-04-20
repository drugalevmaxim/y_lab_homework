package xyz.drugalev.usecase.training;

import xyz.drugalev.domain.entity.User;
import xyz.drugalev.domain.service.TrainingService;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Map;

/**
 * Use case for getting training stats.
 *
 * @author Drugalev Maxim
 */
public class GetTrainingStatsUseCase {
    private final TrainingService trainingService;

    /**
     * Default constructor.
     *
     * @param trainingService service that use case works with.
     */
    public GetTrainingStatsUseCase(TrainingService trainingService) {
        this.trainingService = trainingService;
    }

    /**
     * Returns training stats in Map with "Duration" and "Calories" keys
     *
     * @param user  user who performed training.
     * @param date1 starting date.
     * @param date2 ending date.
     * @return Map with "Duration" and "Calories" keys.
     */
    public Map<String, Integer> getTrainingsStats(User user, LocalDate date1, LocalDate date2) throws SQLException {
        return trainingService.getTrainingsStats(user, date1, date2);
    }

}
