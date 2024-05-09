package xyz.drugalev.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import xyz.drugalev.dto.TrainingDto;
import xyz.drugalev.entity.Training;

import java.util.List;

/**
 * Mapper for converting between {@link Training} and {@link TrainingDto}.
 */
@Mapper
public interface TrainingMapper {

    /**
     * Converts a single {@link Training} entity to a {@link TrainingDto}.
     *
     * @param training The {@link Training} entity to convert.
     * @return The converted {@link TrainingDto}.
     */
    TrainingDto toTrainingDto(Training training);

    /**
     * Converts a single {@link TrainingDto} to a {@link Training} entity.
     *
     * @param trainingDto The {@link TrainingDto} to convert.
     * @return The converted {@link Training} entity.
     */
    @Mapping(target = "performer", ignore = true)
    Training toTraining(TrainingDto trainingDto);

    /**
     * Converts a list of {@link Training} entities to a list of {@link TrainingDto}s.
     *
     * @param trainings The list of {@link Training} entities to convert.
     * @return The converted list of {@link TrainingDto}s.
     */
    List<TrainingDto> toTrainingDtos(List<Training> trainings);
}