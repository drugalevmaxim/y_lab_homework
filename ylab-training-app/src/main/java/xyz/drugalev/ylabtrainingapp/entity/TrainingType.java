package xyz.drugalev.ylabtrainingapp.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Training type entity.
 *
 * @author Drugalev Maxim
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TrainingType {
    private long id;
    private String name;
}
