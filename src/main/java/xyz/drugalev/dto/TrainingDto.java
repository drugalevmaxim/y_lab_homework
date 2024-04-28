package xyz.drugalev.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TrainingDto {
        private long id;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        private LocalDate date;
        private TrainingTypeDto trainingType;
        private final Set<TrainingDataDto> trainingData  = new HashSet<>();
        private int duration;
        private int burnedCalories;
}
