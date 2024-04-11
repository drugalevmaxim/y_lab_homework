package xyz.drugalev.usecase.training;

import xyz.drugalev.domain.entity.User;
import xyz.drugalev.domain.entity.Training;
import xyz.drugalev.domain.exception.IllegalDatePeriodException;
import xyz.drugalev.domain.service.TrainingService;

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
    public List<Training> findAll() {
        return trainingService.findAll();
    }

    /**
     * Returns list of all users trainings.
     *
     * @param user user who performed training.
     * @return list of all trainings.
     */
    public List<Training> findAllByUser(User user) {
        return trainingService.findAllByUser(user);
    }

    /**
     * Returns list of all users trainings performed between given dates.
     *
     * @param user user who performed training.
     * @param start starting date.
     * @param end ending date.
     * @return list of all trainings.
     * @throws IllegalDatePeriodException if start > end
     */
    public List<Training> findBetween(User user, LocalDate start, LocalDate end) throws IllegalDatePeriodException {
        if (start.isAfter(end)) {
            throw new IllegalDatePeriodException("Start date is later than end date. ");
        }

        return trainingService.findByUserBetweenDates(user, start, end);
    }
}
