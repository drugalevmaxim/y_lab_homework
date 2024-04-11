package xyz.drugalev.domain.entity;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Training entity.
 *
 * @author Drugalev Maxim
 */
@Getter
public class Training {
    private final User performer;
    private final LocalDate date;
    private final TrainingType trainingType;
    private final List<TrainingData> trainingData;
    @Setter
    private int duration;
    @Setter
    private int burnedCalories;

    /**
     * Default constructor
     *
     * @param performer user which performed training.
     * @param date date when training was performed.
     * @param trainingType type of training.
     * @param duration duration of training.
     * @param burnedCalories burned calories during training.
     */
    public Training(User performer, LocalDate date, TrainingType trainingType, int duration, int burnedCalories) {
        this.performer = performer;
        this.date = date;
        this.trainingType = trainingType;
        this.duration = duration;
        this.burnedCalories = burnedCalories;
        trainingData = new ArrayList<>();
    }

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
