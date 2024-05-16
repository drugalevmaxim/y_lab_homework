package xyz.drugalev.ylabtrainingapp.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
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
 * Training DTO.
 *
 * @author Drugalev Maxim
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "Training")
public class TrainingDto {
    private long id;
    private String user;
    @PastOrPresent
    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate date;
    @NotNull
    private TrainingTypeDto type;
    @Positive
    private int duration;
    @Positive
    private int burnedCalories;
    private final Set<TrainingDataDto> trainingData = new HashSet<>();
}
