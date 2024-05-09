package xyz.drugalev.validator;

import xyz.drugalev.dto.TrainingDto;

import java.time.LocalDate;

/**
 * Validator for {@link TrainingDto}.
 */
public class TrainingDtoValidator {

    /**
     * Validates a {@link TrainingDto}.
     *
     * @param trainingDto The {@link TrainingDto} to validate.
     * @return True if the {@link TrainingDto} is valid, false otherwise.
     */
    public static boolean isValid(TrainingDto trainingDto) {
        if (trainingDto == null) {
            return false;
        }
        if (trainingDto.getTrainingType() == null) {
            return false;
        }
        if (trainingDto.getDate() == null || trainingDto.getDate().isAfter(LocalDate.now())) {
            return false;
        }
        return trainingDto.getDuration() >= 0 && trainingDto.getBurnedCalories() >= 0;
    }

}