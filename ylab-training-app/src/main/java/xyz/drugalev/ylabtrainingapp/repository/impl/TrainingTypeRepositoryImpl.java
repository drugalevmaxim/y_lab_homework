package xyz.drugalev.ylabtrainingapp.repository.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import xyz.drugalev.ylablogspringbootstarter.aspect.Loggable;
import xyz.drugalev.ylabtrainingapp.entity.TrainingType;
import xyz.drugalev.ylabtrainingapp.repository.TrainingTypeRepository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
@Loggable
public class TrainingTypeRepositoryImpl implements TrainingTypeRepository {
    private final JdbcTemplate jdbcTemplate;

    @Value("${spring.liquibase.default-schema}")
    private String schemaName;

    @Override
    public TrainingType save(TrainingType trainingType) {
        SimpleJdbcInsert insert = new SimpleJdbcInsert(jdbcTemplate)
                .withSchemaName(schemaName)
                .withTableName("training_type")
                .usingGeneratedKeyColumns("id");
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("name", trainingType.getName());
        long id = insert.executeAndReturnKey(params).longValue();
        trainingType.setId(id);
        return trainingType;
    }

    @Override
    public List<TrainingType> findAll() {
        String sql = """
                SELECT *
                FROM training_type
                """;
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(TrainingType.class));
    }

    @Override
    public Optional<TrainingType> findById(long id) {
        String sql = """
                SELECT *
                FROM training_type WHERE id = ? LIMIT 1
                """;
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(TrainingType.class), id));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }
}
