package xyz.drugalev.mapper;

import org.mapstruct.Mapper;
import xyz.drugalev.dto.TrainingDto;
import xyz.drugalev.entity.Training;
import xyz.drugalev.entity.User;

import java.util.List;

/**
 * Mapper for converting between {@link Training} and {@link TrainingDto}.
 */
@Mapper(componentModel = "spring")
public interface TrainingMapper {

    /**
     * Converts a single {@link Training} entity to a {@link TrainingDto}.
     *
     * @param training The {@link Training} entity to convert.
     * @return The converted {@link TrainingDto}.
     */
    TrainingDto toTrainingDto(Training training);

    /**
     * Converts a list of {@link Training} entities to a list of {@link TrainingDto}s.
     *
     * @param trainings The list of {@link Training} entities to convert.
     * @return The converted list of {@link TrainingDto}s.
     */
    List<TrainingDto> toTrainingDtoList(List<Training> trainings);

    /**
     * Converts a single {@link TrainingDto} to a {@link Training} entity.
     *
     * @param trainingDto The {@link TrainingDto} to convert.
     * @return The converted {@link Training} entity.
     */
    Training toTraining(TrainingDto trainingDto);

    /**
     * Maps the username of a {@link User} to a {@link String}.
     *
     * @param value The {@link User} to map.
     * @return The mapped {@link String}.
     */
    default String mapUsername(User value) {
        return value.getUsername();
    }

    /**
     * Maps a {@link String} to a {@link User}.
     *
     * @param value The {@link String} to map.
     * @return The mapped {@link User}.
     */
    default User mapUser(String value) {
        User user = new User();
        user.setUsername(value);
        return user;
    }
}