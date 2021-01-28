package com.github.astappiev.jdbcperf.impl;

import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.Jdbi;

import com.github.astappiev.jdbcperf.AbstractJdbcLib;
import com.github.astappiev.jdbcperf.entity.User;

public class Jdbi3 extends AbstractJdbcLib {
    private Jdbi jdbi;
    private Handle handle;

    @Override
    public void init() {
        jdbi = Jdbi.create(dataSource);
        jdbi.registerRowMapper(User.class, (rs, ctx) -> {
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
        });
    }

    @Override
    protected void open() throws Exception {
        handle = jdbi.open();
    }

    @Override
    public User select(int id) {
        return handle.select("SELECT * FROM user WHERE id = ?", id)
            .mapTo(User.class)
            .findOne().orElse(null);
    }

    @Override
    protected Integer insert(final User user) throws Exception {
        return handle.createUpdate("insert into user (first_name, last_name, address, zip_code, city, birthday, created_at) values (?, ?, ?, ?, ?, ?, ?)")
            .bind(0, user.getFirstName())
            .bind(1, user.getLastName())
            .bind(2, user.getAddress())
            .bind(3, user.getZipCode())
            .bind(4, user.getCity())
            .bind(5, user.getBirthday())
            .bind(6, user.getCreatedAt())
            .executeAndReturnGeneratedKeys()
            .mapTo(Integer.class)
            .one();
    }

    @Override
    protected void close() throws Exception {
        handle.close();
    }
}