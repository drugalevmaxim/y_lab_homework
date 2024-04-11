package xyz.drugalev;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import xyz.drugalev.domain.entity.TrainingType;
import xyz.drugalev.domain.repository.TrainingTypeRepositoryImpl;
import xyz.drugalev.domain.service.TrainingTypeService;
import xyz.drugalev.domain.service.TrainingTypeServiceImpl;
import xyz.drugalev.usecase.trainingtype.AddTrainingTypeUseCase;
import xyz.drugalev.usecase.trainingtype.FindTrainingTypeUseCase;

public class TrainingTypeTest {

    TrainingTypeService trainingTypeService;

    @BeforeEach
    public void setUp() {
        trainingTypeService = new TrainingTypeServiceImpl(new TrainingTypeRepositoryImpl());
    }

    @Test
    public void addTrainingType() {
        String name = "Training";
        AddTrainingTypeUseCase useCase = new AddTrainingTypeUseCase(trainingTypeService);
        useCase.add(name);
        Assertions.assertTrue(trainingTypeService.findAll().stream().anyMatch(tt -> tt.getName().equals(name)));
    }

    @Test
    public void getAllTenTrainingTypes() {
        FindTrainingTypeUseCase useCase = new FindTrainingTypeUseCase(trainingTypeService);
        for (int i = 0; i < 10; i++) {
            trainingTypeService.save(new TrainingType(String.valueOf(i)));
        }
        Assertions.assertEquals(10, useCase.findAll().size());
    }
}
