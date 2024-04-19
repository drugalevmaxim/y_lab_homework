package xyz.drugalev.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Training type entity
 *
 * @author Drugalev Maxim
 */
@Data
@AllArgsConstructor
public class TrainingType {
    private final String name;

    /**
     * Represents training type as string.
     *
     * @return string representation.
     */
    @Override
    public String toString() {
        return this.name;
    }
}
