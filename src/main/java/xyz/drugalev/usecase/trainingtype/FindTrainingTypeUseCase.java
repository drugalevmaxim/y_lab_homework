package xyz.drugalev.usecase.trainingtype;

import xyz.drugalev.domain.entity.TrainingType;
import xyz.drugalev.domain.service.TrainingTypeService;

import java.util.List;

/**
 * Use case for listing training type.
 *
 * @author Drugalev Maxim
 */
public class FindTrainingTypeUseCase {
    private final TrainingTypeService trainingTypeService;

    /**
     * Default constructor
     *
     * @param trainingTypeService service that use case works with.
     */
    public FindTrainingTypeUseCase(TrainingTypeService trainingTypeService) {
        this.trainingTypeService = trainingTypeService;
    }

    /**
     * Returns list of all training types
     *
     * @return list of all training types
     */
    public List<TrainingType> findAll() {
        return trainingTypeService.findAll();
    }
}
