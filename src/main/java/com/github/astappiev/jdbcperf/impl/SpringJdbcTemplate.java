package com.github.astappiev.jdbcperf.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import com.github.astappiev.jdbcperf.AbstractJdbcLib;
import com.github.astappiev.jdbcperf.entity.User;

public class SpringJdbcTemplate extends AbstractJdbcLib {
    private NamedParameterJdbcTemplate jdbcTemplate;

    @Override
    public void init() {
        jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    @Override
    protected void open() throws Exception {
    }

    @Override
    public User select(int id) {
        try {
            return jdbcTemplate.queryForObject("SELECT * FROM user WHERE id = :id", Collections.singletonMap("id", id), new UserRowMapper());
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    protected Integer insert(final User user) throws Exception {
        KeyHolder holder = new GeneratedKeyHolder();
        SqlParameterSource parameters = new MapSqlParameterSource()
            .addValue("firstName", user.getFirstName())
            .addValue("lastName", user.getLastName())
            .addValue("address", user.getAddress())
            .addValue("zipCode", user.getZipCode())
            .addValue("city", user.getCity())
            .addValue("birthday", user.getBirthday())
            .addValue("createdAt", user.getCreatedAt());
        jdbcTemplate.update("insert into user (first_name, last_name, address, zip_code, city, birthday, created_at) "
            + "values (:firstName, :lastName, :address, :zipCode, :city, :birthday, :createdAt)", parameters, holder);
        return holder.getKey().intValue();
    }

    @Override
    protected void close() throws Exception {
    }

    private class UserRowMapper implements RowMapper<User> {
        @Override
        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            User user = new User();
            user.setId(rs.getInt("id"));
            user.setFirstName(rs.getString("first_name"));
            user.setLastName(rs.getString("last_name"));
            user.setAddress(rs.getString("address"));
            user.setZipCode(rs.getInt("zip_code"));
            user.setCity(rs.getString("city"));
            user.setBirthday(rs.getDate("birthday"));
            user.setCreatedAt(rs.getTimestamp("created_at"));
            return user;
        }
    }
}
