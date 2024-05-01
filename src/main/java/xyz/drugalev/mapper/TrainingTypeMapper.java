package xyz.drugalev.mapper;

import org.mapstruct.Mapper;
import xyz.drugalev.dto.TrainingTypeDto;
import xyz.drugalev.entity.TrainingType;

import java.util.List;

/**
 * Mapper for converting between {@link TrainingType} and {@link TrainingTypeDto}.
 */
@Mapper
public interface TrainingTypeMapper {
    /**
     * Converts a list of {@link TrainingType} entities to a list of {@link TrainingTypeDto}s.
     *
     * @param trainingTypes The list of {@link TrainingType} entities to convert.
     * @return The converted list of {@link TrainingTypeDto}s.
     */
    List<TrainingTypeDto> toTrainingTypeDtos(List<TrainingType> trainingTypes);
}
