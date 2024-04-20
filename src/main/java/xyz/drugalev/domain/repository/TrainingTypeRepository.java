package xyz.drugalev.domain.repository;

import xyz.drugalev.domain.entity.TrainingType;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface TrainingTypeRepository {
    Optional<TrainingType> find(int id) throws SQLException;
    Optional<TrainingType> find(String trainingTypeName) throws SQLException;
    List<TrainingType> findAll() throws SQLException;
    void save(String trainingTypeName) throws SQLException;
}
