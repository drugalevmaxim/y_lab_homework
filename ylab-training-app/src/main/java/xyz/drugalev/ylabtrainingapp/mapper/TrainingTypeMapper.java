package xyz.drugalev.ylabtrainingapp.mapper;

import org.mapstruct.Mapper;
import xyz.drugalev.ylabtrainingapp.dto.TrainingTypeDto;
import xyz.drugalev.ylabtrainingapp.entity.TrainingType;

import java.util.List;

/**
 * Training Type mapper.
 *
 * @author Drugalev Maxim
 */
@Mapper(componentModel = "spring")
public interface TrainingTypeMapper {
    TrainingTypeDto toTrainingTypeDto(TrainingType trainingType);

    List<TrainingTypeDto> toTrainingTypeDtos(List<TrainingType> trainingTypes);

    TrainingType toTrainingType(TrainingTypeDto trainingTypeDto);
}