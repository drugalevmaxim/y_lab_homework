package xyz.drugalev.ylabtrainingapp.repository.impl.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import xyz.drugalev.ylabtrainingapp.entity.Training;
import xyz.drugalev.ylabtrainingapp.entity.TrainingData;
import xyz.drugalev.ylabtrainingapp.repository.TrainingTypeRepository;
import xyz.drugalev.ylabtrainingapp.repository.UserRepository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class TrainingRowMapper implements RowMapper<Training> {
    private final UserRepository userRepository;
    private final TrainingTypeRepository trainingTypeRepository;
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Training mapRow(ResultSet rs, int rowNum) throws SQLException {
        return Training.builder()
                .id(rs.getLong("id"))
                .user(userRepository.findById(rs.getLong("person_id")).orElseThrow())
                .type(trainingTypeRepository.findById(rs.getLong("training_type_id")).orElseThrow())
                .date(rs.getDate("training_date").toLocalDate())
                .duration(rs.getLong("duration"))
                .burnedCalories(rs.getLong("burned_calories"))
                .trainingData(Set.copyOf(jdbcTemplate.query("SELECT * FROM training_data WHERE training_id = ?;", new BeanPropertyRowMapper<>(TrainingData.class), rs.getLong("id"))))
                .build();
    }
}
