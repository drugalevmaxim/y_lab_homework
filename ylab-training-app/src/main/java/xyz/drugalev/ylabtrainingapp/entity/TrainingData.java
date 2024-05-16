package xyz.drugalev.ylabtrainingapp.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Training data entity.
 *
 * @author Drugalev Maxim
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TrainingData {
    private String name;
    private long value;
}
