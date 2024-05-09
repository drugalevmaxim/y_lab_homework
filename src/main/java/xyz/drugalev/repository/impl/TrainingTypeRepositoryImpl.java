package xyz.drugalev.repository.impl;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import xyz.drugalev.entity.TrainingType;
import xyz.drugalev.repository.TrainingTypeRepository;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;

@Repository
public class TrainingTypeRepositoryImpl implements TrainingTypeRepository {
    final JdbcTemplate jdbcTemplate;

    public TrainingTypeRepositoryImpl(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public Optional<TrainingType> findById(long id) {
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject("SELECT * from ylab_trainings.training_types where id = ?;", new BeanPropertyRowMapper<>(TrainingType.class), id));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<TrainingType> findByName(String trainingTypeName) {
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject("SELECT * from ylab_trainings.training_types where name = ?;", new BeanPropertyRowMapper<>(TrainingType.class), trainingTypeName));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<TrainingType> findAll() {
        return jdbcTemplate.query("SELECT * from ylab_trainings.training_types;", new BeanPropertyRowMapper<>(TrainingType.class));
    }

    @Override
    public TrainingType save(TrainingType trainingType) {
        SimpleJdbcInsert insert = new SimpleJdbcInsert(jdbcTemplate)
                .withSchemaName("ylab_trainings")
                .withTableName("training_types")
                .usingGeneratedKeyColumns("id");
        SqlParameterSource parameters = new BeanPropertySqlParameterSource(trainingType);
        return findById(insert.executeAndReturnKey(parameters).longValue()).orElseThrow();
    }
}
