package xyz.drugalev.ylabtrainingapp.repository.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import xyz.drugalev.ylablogspringbootstarter.aspect.Loggable;
import xyz.drugalev.entity.Audit;
import xyz.drugalev.repository.AuditRepository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
@Loggable
public class AuditRepositoryImpl implements AuditRepository {
    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Audit> auditRowMapper;

    @Value("${spring.liquibase.default-schema}")
    private String schemaName;

    @Override
    public Audit save(Audit audit) {
        SimpleJdbcInsert insert = new SimpleJdbcInsert(jdbcTemplate)
                .withSchemaName(schemaName)
                .withTableName("audit");
        audit.setTime(LocalDateTime.now());

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("person_id", audit.getUser().getId());
        params.addValue("action", audit.getAction());
        params.addValue("action_time", audit.getTime());
        insert.execute(params);
        return audit;
    }

    @Override
    public List<Audit> findAll() {
        String sql = """
                SELECT * FROM audit;""";
        return jdbcTemplate.query(sql, auditRowMapper);
    }
}
