package xyz.drugalev.ylabtrainingapp.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Training data DTO.
 *
 * @author Drugalev Maxim
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "Training data")
public class TrainingDataDto {
    @NotNull
    private String name;
    private int value;
}
