package xyz.drugalev.ylabtrainingapp.repository.impl.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import xyz.drugalev.entity.Role;
import xyz.drugalev.entity.User;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
@RequiredArgsConstructor
public class UserRowMapper implements RowMapper<User> {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
        return User.builder()
                .id(rs.getLong("id"))
                .username(rs.getString("username"))
                .password(rs.getString("password"))
                .role(jdbcTemplate.queryForObject("SELECT id, name FROM role WHERE id = ?", new BeanPropertyRowMapper<>(Role.class), rs.getLong("role_id")))
                .build();
    }
}
