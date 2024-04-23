package xyz.drugalev.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * Training entity.
 *
 * @author Drugalev Maxim
 */
@Data
@AllArgsConstructor
public class Training {
    private final int id;
    private final User performer;
    private final LocalDate date;
    private final TrainingType trainingType;
    private final Set<TrainingData> trainingData  = new HashSet<>();
    private int duration;
    private int burnedCalories;
}
