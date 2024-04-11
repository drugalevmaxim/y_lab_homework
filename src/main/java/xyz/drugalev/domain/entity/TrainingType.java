package xyz.drugalev.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Training type entity
 *
 * @author Drugalev Maxim
 */
@AllArgsConstructor
@Getter
public class TrainingType {
    private String name;

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
