package xyz.drugalev.mapper;

import org.mapstruct.Mapper;
import xyz.drugalev.dto.TrainingTypeDto;
import xyz.drugalev.entity.TrainingType;

import java.util.List;

/**
 * Mapper for converting between {@link TrainingType} and {@link TrainingTypeDto}.
 */
@Mapper(componentModel = "spring")
public interface TrainingTypeMapper {

    /**
     * Converts a single {@link TrainingType} entity to a {@link TrainingTypeDto}.
     *
     * @param trainingType The {@link TrainingType} entity to convert.
     * @return The converted {@link TrainingTypeDto}.
     */
    TrainingTypeDto toTrainingTypeDto(TrainingType trainingType);

    /**
     * Converts a list of {@link TrainingType} entities to a list of {@link TrainingTypeDto}s.
     *
     * @param trainingTypes The list of {@link TrainingType} entities to convert.
     * @return The converted list of {@link TrainingTypeDto}s.
     */
    List<TrainingTypeDto> toTrainingTypeDtos(List<TrainingType> trainingTypes);

    /**
     * Converts a single {@link TrainingTypeDto} to a {@link TrainingType} entity.
     *
     * @param trainingTypeDto The {@link TrainingTypeDto} to convert.
     * @return The converted {@link TrainingType} entity.
     */
    TrainingType toTrainingType(TrainingTypeDto trainingTypeDto);
}