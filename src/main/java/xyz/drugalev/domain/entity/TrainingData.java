package xyz.drugalev.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Training data entity
 *
 * @author Drugalev Maxim
 */
@AllArgsConstructor
@Getter
public class TrainingData {
    private String name;
    private int value;

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
