package xyz.drugalev.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Training data entity
 *
 * @author Drugalev Maxim
 */
@Data
@AllArgsConstructor
public class TrainingData {
    private final String name;
    private final int value;

    /**
     * Represents training data as string.
     *
     * @return string representation.
     */
    @Override
    public String toString() {
        return name + ": " + value;
    }
}
