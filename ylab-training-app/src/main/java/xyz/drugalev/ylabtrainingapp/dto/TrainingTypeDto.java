package xyz.drugalev.ylabtrainingapp.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Training type DTO.
 *
 * @author Drugalev Maxim
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "Training type")
public class TrainingTypeDto {
    private int id;
    @NotNull
    private String name;
}
