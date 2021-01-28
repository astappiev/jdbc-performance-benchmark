package com.github.astappiev.jdbcperf.impl;

import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.Jdbi;

import com.github.astappiev.jdbcperf.AbstractJdbcLib;
import com.github.astappiev.jdbcperf.entity.User;

public class Jdbi3Bean extends AbstractJdbcLib {
    private Jdbi jdbi;
    private Handle handle;

    @Override
    public void init() {
        jdbi = Jdbi.create(dataSource);
    }

    @Override
    protected void open() throws Exception {
        handle = jdbi.open();
    }

    @Override
    public User select(int id) {
        return handle.select("SELECT * FROM user WHERE id = ?", id)
            .mapToBean(User.class)
            .findOne().orElse(null);
    }

    @Override
    protected Integer insert(final User user) throws Exception {
        return handle.createUpdate("insert into user (first_name, last_name, address, zip_code, city, birthday, created_at) "
            + "values (:firstName, :lastName, :address, :zipCode, :city, :birthday, :createdAt)")
            .bindBean(user)
            .executeAndReturnGeneratedKeys()
            .mapTo(Integer.class)
            .one();
    }

    @Override
    protected void close() throws Exception {
        handle.close();
    }
}