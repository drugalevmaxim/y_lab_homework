package xyz.drugalev.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Training entity.
 *
 * @author Drugalev Maxim
 */
@Data
@AllArgsConstructor
public class Training {
    private final User performer;
    private final LocalDate date;
    private final TrainingType trainingType;
    private final List<TrainingData> trainingData  = new ArrayList<>();
    private int duration;
    private int burnedCalories;

    /**
     * Represents training as string.
     *
     * @return string representation.
     */
    @Override
    public String toString() {
        return performer.getUsername() + " done " + this.trainingType + " at " + this.date.toString() + " for " + this.duration
                + " min, burned " + this.burnedCalories + " cal";
    }
}
