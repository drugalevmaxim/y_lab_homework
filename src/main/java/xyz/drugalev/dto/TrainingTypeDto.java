package xyz.drugalev.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data transfer object for training types.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TrainingTypeDto {
    private int id;
    @NotNull
    private String name;
}
