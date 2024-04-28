package xyz.drugalev.mapper;

import xyz.drugalev.dto.TrainingDataDto;
import xyz.drugalev.entity.TrainingData;

public interface TrainingDataMapper {
    TrainingDataDto toTrainingDataDto(TrainingData trainingData);
    TrainingData toTrainingData(TrainingDataDto trainingDataDto);
}
