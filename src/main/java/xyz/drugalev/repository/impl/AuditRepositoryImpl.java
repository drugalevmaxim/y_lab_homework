package xyz.drugalev.repository.impl;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import xyz.drugalev.entity.Audit;
import xyz.drugalev.entity.User;
import xyz.drugalev.repository.AuditRepository;
import xyz.drugalev.repository.UserRepository;

import javax.sql.DataSource;
import java.util.HashSet;
import java.util.List;

@Repository
public class AuditRepositoryImpl implements AuditRepository {
    private final JdbcTemplate jdbcTemplate;
    private final UserRepository userRepository;

    public AuditRepositoryImpl(UserRepository userRepository, DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.userRepository = userRepository;
    }

    @Override
    public void save(User user, String action) {
        jdbcTemplate.update("INSERT INTO ylab_trainings.audit (\"user\", \"action\") VALUES (?, ?);", user.getId(), action);
    }

    @Override
    public List<Audit> findAll() {
        return jdbcTemplate.query("SELECT * FROM ylab_trainings.audit;", (rs, rowNum) -> {
            User user = userRepository.findById(rs.getInt("user")).orElse(new User(0, "Unknown", "Unknown", new HashSet<>()));
            return new Audit(user, rs.getString("action"), rs.getTimestamp("time").toLocalDateTime());
        });
    }
}
