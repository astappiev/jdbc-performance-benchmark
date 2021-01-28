package com.github.astappiev.jdbcperf.impl;

import java.util.Collections;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import com.github.astappiev.jdbcperf.AbstractJdbcLib;
import com.github.astappiev.jdbcperf.entity.User;

public class SpringJdbcTemplateBean extends AbstractJdbcLib {
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
            return jdbcTemplate.queryForObject("SELECT * FROM user WHERE id = :id", Collections.singletonMap("id", id), new BeanPropertyRowMapper<>(User.class));
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    protected Integer insert(final User user) throws Exception {
        KeyHolder holder = new GeneratedKeyHolder();
        SqlParameterSource parameters = new BeanPropertySqlParameterSource(user);
        jdbcTemplate.update("insert into user (first_name, last_name, address, zip_code, city, birthday, created_at) "
            + "values (:firstName, :lastName, :address, :zipCode, :city, :birthday, :createdAt)", parameters, holder);
        return holder.getKey().intValue();
    }

    @Override
    protected void close() throws Exception {
    }
}
