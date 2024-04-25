package xyz.drugalev.usecase.trainingtype;

import xyz.drugalev.domain.entity.TrainingType;
import xyz.drugalev.domain.service.TrainingTypeService;

import java.sql.SQLException;
import java.util.List;

/**
 * Use case for listing training type.
 *
 * @author Drugalev Maxim
 */
public class FindTrainingTypeUseCase {
    private final TrainingTypeService trainingTypeService;

    /**
     * Default constructor.
     *
     * @param trainingTypeService service that use case works with.
     */
    public FindTrainingTypeUseCase(TrainingTypeService trainingTypeService) {
        this.trainingTypeService = trainingTypeService;
    }

    /**
     * Returns list of all training types.
     *
     * @return list of all training types
     * @throws SQLException the sql exception
     */
    public List<TrainingType> findAll() throws SQLException {
        return trainingTypeService.findAll();
    }
}
