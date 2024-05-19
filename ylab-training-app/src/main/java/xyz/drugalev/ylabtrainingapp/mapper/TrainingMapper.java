package xyz.drugalev.ylabtrainingapp.mapper;

import org.mapstruct.Mapper;
import xyz.drugalev.entity.User;
import xyz.drugalev.ylabtrainingapp.dto.TrainingDto;
import xyz.drugalev.ylabtrainingapp.entity.Training;

import java.util.List;

/**
 * Training mapper.
 *
 * @author Drugalev Maxim
 */
@Mapper(componentModel = "spring")
public interface TrainingMapper {

    TrainingDto toTrainingDto(Training training);

    List<TrainingDto> toTrainingDtoList(List<Training> trainings);

    Training toTraining(TrainingDto trainingDto);

    default String mapUsername(User value) {
        return value.getUsername();
    }

    default User mapUser(String value) {
        User user = new User();
        user.setUsername(value);
        return user;
    }
}