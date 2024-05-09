package xyz.drugalev.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data transfer object for training data.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TrainingDataDto {
    @NotNull
    private String name;
    private int value;
}
