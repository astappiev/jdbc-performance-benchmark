package com.github.astappiev.jdbcperf.impl;

import org.sql2o.Connection;
import org.sql2o.Query;

import com.github.astappiev.jdbcperf.AbstractJdbcLib;
import com.github.astappiev.jdbcperf.entity.User;

public class Sql2o extends AbstractJdbcLib {
    private org.sql2o.Sql2o sql2o;
    private Connection connection;

    @Override
    public void init() {
        sql2o = new org.sql2o.Sql2o(dataSource);
    }

    @Override
    protected void open() throws Exception {
        connection = sql2o.open();
    }

    @Override
    public User select(int id) {
        try (Query query = connection.createQuery("SELECT * FROM user WHERE id = :id")) {
            return query.addParameter("id", id).executeAndFetchFirst(User.class);
        }
    }

    @Override
    protected Integer insert(final User user) throws Exception {
        try (Query query = connection.createQuery("insert into user (first_name, last_name, address, zip_code, city, birthday, created_at) "
            + "values (:firstName, :lastName, :address, :zipCode, :city, :birthday, :createdAt)")) {
            return query.addParameter("firstName", user.getFirstName())
                .addParameter("lastName", user.getLastName())
                .addParameter("lastName", user.getLastName())
                .addParameter("address", user.getAddress())
                .addParameter("zipCode", user.getZipCode())
                .addParameter("city", user.getCity())
                .addParameter("birthday", user.getBirthday())
                .addParameter("createdAt", user.getCreatedAt())
                .executeUpdate().getKey(Integer.class);
        }
    }

    @Override
    protected void close() throws Exception {
        connection.close();
    }
}
