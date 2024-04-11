package xyz.drugalev.usecase.training;

import xyz.drugalev.domain.entity.User;
import xyz.drugalev.domain.exception.IllegalDatePeriodException;
import xyz.drugalev.domain.service.TrainingService;

import java.time.LocalDate;

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
     * Returns sum of trainings duration at given period.
     *
     * @param user user who performed training.
     * @param start starting date.
     * @param end ending date.
     * @return sum of trainings duration at given period.
     * @throws IllegalDatePeriodException if start > end.
     */
    public int getTrainingsDuration(User user, LocalDate start, LocalDate end) throws IllegalDatePeriodException {
        if (start.isAfter(end)) {
            throw new IllegalDatePeriodException("Start date is later than end date. ");
        }
        return trainingService.getTrainingsDuration(user, start, end);
    }

    /**
     * Returns sum of trainings burned calories at given period.
     *
     * @param user user who performed training.
     * @param start starting date.
     * @param end ending date.
     * @return sum of trainings duration at given period.
     * @throws IllegalDatePeriodException if start > end.
     */
    public int getTrainingsBurnedCalories(User user, LocalDate start, LocalDate end) throws IllegalDatePeriodException {
        if (start.isAfter(end)) {
            throw new IllegalDatePeriodException("Start date is later than end date. ");
        }
        return trainingService.getTrainingsBurnedCalories(user, start, end);
    }

}
