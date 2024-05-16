package xyz.drugalev.ylabtrainingapp.repository.impl.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import xyz.drugalev.entity.Audit;
import xyz.drugalev.ylabtrainingapp.repository.UserRepository;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
@RequiredArgsConstructor
public class AuditRowMapper implements RowMapper<Audit> {
    private final UserRepository userRepository;

    @Override
    public Audit mapRow(ResultSet rs, int rowNum) throws SQLException {
        return Audit.builder()
                .user(userRepository.findById(rs.getLong("person_id")).orElseThrow())
                .action(rs.getString("action"))
                .time(rs.getTimestamp("action_time").toLocalDateTime()).build();
    }
}
