package xyz.drugalev.usecase.trainingtype;

import xyz.drugalev.domain.entity.TrainingType;
import xyz.drugalev.domain.service.TrainingTypeService;

import java.sql.SQLException;

/**
 * Use case for adding training type.
 *
 * @author Drugalev Maxim
 */
public class AddTrainingTypeUseCase {
    private final TrainingTypeService trainingTypeService;

    /**
     * Default constructor
     *
     * @param trainingTypeService service that use case works with.
     */
    public AddTrainingTypeUseCase(TrainingTypeService trainingTypeService) {
        this.trainingTypeService = trainingTypeService;
    }

    /**
     * Saves Training type with given name.
     *
     * @param trainingTypeName Training type name to save.
     * @return saved Training type.
     */
    public TrainingType add(String trainingTypeName) throws SQLException {
        trainingTypeService.save(trainingTypeName);
        return trainingTypeService.find(trainingTypeName).get();
    }
}
