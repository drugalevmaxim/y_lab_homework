package xyz.drugalev.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TrainingData {
    private final int id;
    private final String name;
    private final int value;
}
