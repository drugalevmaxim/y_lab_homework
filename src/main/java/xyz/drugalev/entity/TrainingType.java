package xyz.drugalev.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Training type entity.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TrainingType {
    private long id;
    private String name;
}
