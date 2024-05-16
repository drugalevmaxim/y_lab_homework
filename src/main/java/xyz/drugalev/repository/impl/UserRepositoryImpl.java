package xyz.drugalev.repository.impl;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import xyz.drugalev.entity.Privilege;
import xyz.drugalev.entity.Role;
import xyz.drugalev.entity.User;
import xyz.drugalev.repository.UserRepository;

import javax.sql.DataSource;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Repository
public class UserRepositoryImpl implements UserRepository {
    final JdbcTemplate jdbcTemplate;

    public UserRepositoryImpl(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public Optional<User> findById(long id) {
        try {
            User user = jdbcTemplate.queryForObject("SELECT * from ylab_trainings.users where id = ?;", new BeanPropertyRowMapper<>(User.class), id);
            if (user == null) {
                return Optional.empty();
            }
            return Optional.of(mapUserRoles(user));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public User save(User user) {
        SimpleJdbcInsert insert = new SimpleJdbcInsert(jdbcTemplate)
                .withSchemaName("ylab_trainings")
                .withTableName("users")
                .usingGeneratedKeyColumns("id");
        SqlParameterSource parameters = new BeanPropertySqlParameterSource(user);
        return findById(insert.executeAndReturnKey(parameters).longValue()).orElseThrow();
    }

    @Override
    public Optional<User> findByName(String name) {
        try {
            User user = jdbcTemplate.queryForObject("SELECT * from ylab_trainings.users where username = ?;", new BeanPropertyRowMapper<>(User.class), name);
            if (user == null) {
                return Optional.empty();
            }
            return Optional.of(mapUserRoles(user));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    private User mapUserRoles(User user) {
        List<Role> roles = jdbcTemplate.query("SELECT * from ylab_trainings.roles where id in (SELECT role_id from ylab_trainings.user_roles where user_id = ?);", new BeanPropertyRowMapper<>(Role.class), user.getId());
        for (Role role : roles) {
            List<Privilege> privileges = jdbcTemplate.query("SELECT * from ylab_trainings.privileges where id in (SELECT privilege_id from ylab_trainings.role_privileges where role_id = ?);", new BeanPropertyRowMapper<>(Privilege.class), role.getId());
            role.setPrivileges(new HashSet<>(privileges));
        }
        user.setRoles(new HashSet<>(roles));
        return user;
    }
}