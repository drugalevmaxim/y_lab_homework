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
    private final int id;
    private final String name;
}
