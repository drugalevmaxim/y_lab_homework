package xyz.drugalev.mapper;

import org.mapstruct.Mapper;
import xyz.drugalev.dto.TrainingDataDto;
import xyz.drugalev.entity.TrainingData;

/**
 * Mapper for converting between {@link TrainingData} and {@link TrainingDataDto}.
 */
@Mapper
public interface TrainingDataMapper {

    /**
     * Converts a single {@link TrainingData} entity to a {@link TrainingDataDto}.
     *
     * @param trainingData The {@link TrainingData} entity to convert.
     * @return The converted {@link TrainingDataDto}.
     */
    TrainingDataDto toTrainingDataDto(TrainingData trainingData);

    /**
     * Converts a single {@link TrainingDataDto} to a {@link TrainingData} entity.
     *
     * @param trainingDataDto The {@link TrainingDataDto} to convert.
     * @return The converted {@link TrainingData} entity.
     */
    TrainingData toTrainingData(TrainingDataDto trainingDataDto);
}