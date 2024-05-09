package xyz.drugalev.dto;

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
    private String name;
}
