package xyz.drugalev.dto;

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
    private String name;
    private int value;
}
