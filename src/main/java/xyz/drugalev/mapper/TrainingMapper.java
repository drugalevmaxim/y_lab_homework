package xyz.drugalev.mapper;

import org.mapstruct.Mapper;
import xyz.drugalev.dto.TrainingDto;
import xyz.drugalev.entity.Training;

import java.util.List;

@Mapper
public interface TrainingMapper {
    TrainingDto toTrainingDto(Training training);
    Training toTraining(TrainingDto trainingDto);
    List<TrainingDto> toTrainingDtos(List<Training> trainings);
}