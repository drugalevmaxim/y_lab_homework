package xyz.drugalev.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * Training entity.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Training {
    private int id;
    private User performer;
    private LocalDate date;
    private TrainingType trainingType;
    private final Set<TrainingData> trainingData  = new HashSet<>();
    private int duration;
    private int burnedCalories;
}
