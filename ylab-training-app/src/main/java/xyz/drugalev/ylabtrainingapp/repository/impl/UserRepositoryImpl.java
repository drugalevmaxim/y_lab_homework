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
import xyz.drugalev.ylabtrainingapp.repository.UserRepository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
@Loggable
public class UserRepositoryImpl implements UserRepository {
    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<User> userRowMapper;

    @Value("${spring.liquibase.default-schema}")
    private String schema;

    @Override
    public User save(User user) {
        SimpleJdbcInsert insert = new SimpleJdbcInsert(jdbcTemplate)
                .withSchemaName(schema)
                .withTableName("person")
                .usingGeneratedKeyColumns("id");
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("username", user.getUsername());
        params.addValue("password", user.getPassword());
        params.addValue("role_id", user.getRole().getId());
        long id = insert.executeAndReturnKey(params).longValue();
        user.setId(id);
        return user;
    }

    @Override
    public Optional<User> findById(Long id) {
        String sql = """
                SELECT * FROM person WHERE id = ? LIMIT 1
                """;
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, userRowMapper, id));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<User> findByUsername(String username) {
        String sql = """
                SELECT * FROM person WHERE username = ? LIMIT 1
                """;
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, userRowMapper, username));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }
}
