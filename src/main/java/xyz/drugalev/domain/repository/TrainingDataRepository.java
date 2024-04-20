package xyz.drugalev.domain.repository;

import xyz.drugalev.domain.entity.Training;
import xyz.drugalev.domain.entity.TrainingData;

import java.sql.SQLException;
import java.util.List;

public interface TrainingDataRepository {
    void save(Training training, String name, int value) throws SQLException;

    List<TrainingData> find(Training training) throws SQLException;

}
