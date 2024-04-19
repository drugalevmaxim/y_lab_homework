package xyz.drugalev.usecase.training;

import xyz.drugalev.domain.entity.User;
import xyz.drugalev.domain.exception.IllegalDatePeriodException;
import xyz.drugalev.domain.service.TrainingService;

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
     * @param start starting date.
     * @param end   ending date.
     * @return Map with "Duration" and "Calories" keys.
     * @throws IllegalDatePeriodException if start > end.
     */
    public Map<String, Integer> getTrainingsStats(User user, LocalDate start, LocalDate end) throws IllegalDatePeriodException {
        if (start.isAfter(end)) {
            throw new IllegalDatePeriodException("Start date is later than end date. ");
        }
        return trainingService.getTrainingsStats(user, start, end);
    }

}
