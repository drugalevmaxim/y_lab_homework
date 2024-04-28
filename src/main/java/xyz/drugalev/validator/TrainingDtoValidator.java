package xyz.drugalev.validator;

import xyz.drugalev.dto.TrainingDto;

import java.time.LocalDate;

public class TrainingDtoValidator {
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
        if (trainingDto.getDuration() < 0  || trainingDto.getBurnedCalories() < 0) {
            return false;
        }
        return true;
    }

}
