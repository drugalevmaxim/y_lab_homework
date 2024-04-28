package xyz.drugalev.mapper;

import org.mapstruct.Mapper;
import xyz.drugalev.dto.TrainingTypeDto;
import xyz.drugalev.entity.TrainingType;

import java.util.List;

@Mapper
public interface TrainingTypeMapper {
    TrainingTypeDto toTrainingTypeDto(String name);
    TrainingType toTrainingType(TrainingTypeDto trainingTypeDto);
    List<TrainingTypeDto> toTrainingTypeDtos(List<TrainingType> trainingType);
}
