package xyz.drugalev.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Training data entity.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TrainingData {
    private String name;
    private long value;
}
