package xyz.drugalev.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * Data transfer object for trainings.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TrainingDto {
        private long id;
        private String user;
        @PastOrPresent
        @NotNull
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        private LocalDate date;
        @NotNull
        private TrainingTypeDto trainingType;
        private final Set<TrainingDataDto> trainingData  = new HashSet<>();
        @Positive
        private int duration;
        @Positive
        private int burnedCalories;
}
