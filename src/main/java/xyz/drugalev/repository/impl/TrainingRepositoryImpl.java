package xyz.drugalev.repository.impl;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import xyz.drugalev.entity.Training;
import xyz.drugalev.entity.TrainingData;
import xyz.drugalev.entity.TrainingType;
import xyz.drugalev.entity.User;
import xyz.drugalev.repository.TrainingRepository;

import javax.sql.DataSource;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public class TrainingRepositoryImpl implements TrainingRepository {
    private JdbcTemplate jdbcTemplate;
    private final RowMapper<Training> trainingRowMapper = (resultSet, rowNum) -> {
        Training training = new Training();
        training.setId(resultSet.getLong("id"));
        training.setDate(resultSet.getDate("date").toLocalDate());
        training.setBurnedCalories(resultSet.getInt("burned_calories"));
        training.setDuration(resultSet.getInt("duration"));
        training.setUser(jdbcTemplate.queryForObject("SELECT * FROM ylab_trainings.users WHERE id = ?;",
                new BeanPropertyRowMapper<>(User.class),
                resultSet.getLong("user_id")));
        training.setTrainingType(jdbcTemplate.queryForObject("SELECT * FROM ylab_trainings.training_types WHERE id = ?;",
                new BeanPropertyRowMapper<>(TrainingType.class),
                resultSet.getLong("training_type")));
        training.getTrainingData().addAll(jdbcTemplate.query("SELECT * FROM ylab_trainings.training_data WHERE training = ?;",
                new BeanPropertyRowMapper<>(TrainingData.class),
                resultSet.getLong("id")));
        return training;
    };

    public TrainingRepositoryImpl(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public List<Training> findAll() {
        return jdbcTemplate.query("SELECT * FROM ylab_trainings.trainings ORDER BY date DESC;", trainingRowMapper);
    }

    @Override
    public Optional<Training> find(long id) {
        try {
            Training t = jdbcTemplate.queryForObject("SELECT * FROM ylab_trainings.trainings WHERE id = ?;", trainingRowMapper, id);
            if (t == null) {
                return Optional.empty();
            }
            return Optional.of(t);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Training> findAllByUser(User user) {
        return jdbcTemplate.query("SELECT * FROM ylab_trainings.trainings WHERE user_id = ? ORDER BY date DESC;", trainingRowMapper, user.getId());
    }

    @Override
    public List<Training> findByUserBetweenDates(User user, LocalDate start, LocalDate end) {
        return jdbcTemplate.query("SELECT * FROM ylab_trainings.trainings WHERE user_id = ? and date BETWEEN ? and ? ORDER BY date DESC;", trainingRowMapper, user.getId(), start, end);
    }

    @Override
    public Optional<Training> find(User user, TrainingType type, LocalDate date) {
        try {
            Training t = jdbcTemplate.queryForObject("SELECT * FROM ylab_trainings.trainings WHERE user_id = ? and training_type = ? and date = ?;", trainingRowMapper, user.getId(), type.getId(), date);
            if (t == null) {
                return Optional.empty();
            }
            return Optional.of(t);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Training save(Training training) {
        SimpleJdbcInsert insert = new SimpleJdbcInsert(jdbcTemplate)
                .withSchemaName("ylab_trainings")
                .withTableName("trainings")
                .usingGeneratedKeyColumns("id");
        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue("user_id", training.getUser().getId());
        namedParameters.addValue("date", training.getDate());
        namedParameters.addValue("training_type", training.getTrainingType().getId());
        namedParameters.addValue("duration", training.getDuration());
        namedParameters.addValue("burned_calories", training.getBurnedCalories());
        training.setId(insert.executeAndReturnKey(namedParameters).longValue());
        training.getTrainingData().forEach(t -> jdbcTemplate.update("INSERT INTO ylab_trainings.training_data (training, name, value) VALUES (?, ? , ?)", training.getId(), t.getName(), t.getValue()));
        return find(training.getId()).orElseThrow();
    }

    @Override
    public void delete(Training training) {
        jdbcTemplate.update("DELETE FROM ylab_trainings.trainings WHERE id = ?", training.getId());
    }

    @Override
    public Training update(Training training) {
        jdbcTemplate.update("UPDATE ylab_trainings.trainings SET date = ?, training_type = ?, duration = ?, burned_calories = ? WHERE id = ?;",
                training.getDate(), training.getTrainingType().getId(), training.getBurnedCalories(), training.getDuration(), training.getId());
        jdbcTemplate.update("DELETE FROM ylab_trainings.training_data WHERE training = ?", training.getId());
        training.getTrainingData().forEach(t -> jdbcTemplate.update("INSERT INTO ylab_trainings.training_data (training, name, value) VALUES (?, ? , ?)", training.getId(), t.getName(), t.getValue()));
        return find(training.getId()).orElseThrow();
    }
}
