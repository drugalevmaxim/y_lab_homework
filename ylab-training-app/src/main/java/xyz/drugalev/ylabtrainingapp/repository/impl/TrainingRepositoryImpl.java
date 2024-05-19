package xyz.drugalev.ylabtrainingapp.repository.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import xyz.drugalev.entity.User;
import xyz.drugalev.ylablogspringbootstarter.aspect.Loggable;
import xyz.drugalev.ylabtrainingapp.entity.Training;
import xyz.drugalev.ylabtrainingapp.repository.TrainingRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
@Loggable
public class TrainingRepositoryImpl implements TrainingRepository {
    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Training> trainingRowMapper;

    @Value("${spring.liquibase.default-schema}")
    private String schema;

    @Override
    public List<Training> findAll() {
        String sql = """
                SELECT * FROM training
                """;
        return jdbcTemplate.query(sql, trainingRowMapper);
    }

    @Override
    public Optional<Training> findById(long id) {
        String sql = """
                SELECT * FROM training WHERE id = ? LIMIT 1
                """;
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, trainingRowMapper, id));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Training> findAllByUser(User user) {
        String sql = """
                SELECT * FROM training WHERE person_id = ?
                """;
        return jdbcTemplate.query(sql, trainingRowMapper, user.getId());
    }

    @Override
    public List<Training> findAllByUserBetweenDates(User user, LocalDate start, LocalDate end) {
        String sql = """
                SELECT * FROM training WHERE person_id = ? and training_date BETWEEN ? and ?
                """;
        return jdbcTemplate.query(sql, trainingRowMapper, user.getId(), start, end);
    }


    @Override
    public Training save(Training training) {
        SimpleJdbcInsert insertTraining = new SimpleJdbcInsert(jdbcTemplate)
                .withSchemaName(schema)
                .withTableName("training")
                .usingGeneratedKeyColumns("id");
        MapSqlParameterSource trainingParams = new MapSqlParameterSource();
        trainingParams.addValue("person_id", training.getUser().getId());
        trainingParams.addValue("training_date", training.getDate());
        trainingParams.addValue("training_type_id", training.getType().getId());
        trainingParams.addValue("duration", training.getDuration());
        trainingParams.addValue("burned_calories", training.getBurnedCalories());
        training.setId(insertTraining.executeAndReturnKey(trainingParams).longValue());
        training.getTrainingData().forEach(t -> jdbcTemplate.update("INSERT INTO training_data (training_id, name, value) VALUES (?, ? , ?)"
                , training.getId(), t.getName(), t.getValue()));
        return training;
    }

    @Override
    public void delete(Training training) {
        jdbcTemplate.update("DELETE FROM training WHERE id = ?", training.getId());
    }

    @Override
    public Training update(Training training) {
        jdbcTemplate.update("UPDATE training SET training_date = ?, training_type_id = ?, duration = ?, burned_calories = ? WHERE id = ?;",
                training.getDate(), training.getType().getId(), training.getBurnedCalories(), training.getDuration(), training.getId());
        jdbcTemplate.update("DELETE FROM training_data WHERE training_id = ?", training.getId());
        training.getTrainingData().forEach(t -> jdbcTemplate.update("INSERT INTO training_data (training_id, name, value) VALUES (?, ? , ?)", training.getId(), t.getName(), t.getValue()));
        return training;
    }
}
