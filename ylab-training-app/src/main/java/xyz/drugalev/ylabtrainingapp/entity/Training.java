package xyz.drugalev.ylabtrainingapp.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import xyz.drugalev.entity.User;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * Training entity.
 *
 * @author Drugalev Maxim
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Training {
    private long id;
    private User user;
    private LocalDate date;
    private TrainingType type;
    private long duration;
    private long burnedCalories;
    @Builder.Default
    private Set<TrainingData> trainingData = new HashSet<>();

}
