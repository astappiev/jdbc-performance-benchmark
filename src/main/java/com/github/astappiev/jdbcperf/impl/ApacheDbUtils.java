package com.github.astappiev.jdbcperf.impl;

import java.sql.Connection;
import java.sql.SQLException;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import com.github.astappiev.jdbcperf.AbstractJdbcLib;
import com.github.astappiev.jdbcperf.entity.User;

public class ApacheDbUtils extends AbstractJdbcLib {
    private QueryRunner run;

    private Connection connection;

    @Override
    public void init() {
        run = new QueryRunner();
    }

    @Override
    protected void open() throws Exception {
        connection = dataSource.getConnection();
    }

    @Override
    public User select(int id) throws SQLException {
        return run.query(connection, "SELECT * FROM user WHERE id = ?", rs -> {
            if (rs.next()) {
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
            return null;
        }, id);
    }

    @Override
    protected Integer insert(final User user) throws Exception {
        Object result = run.insert(connection, "insert into user (first_name, last_name, address, zip_code, city, birthday, created_at) values (?, ?, ?, ?, ?, ?, ?)", new ScalarHandler<Long>(),
            user.getFirstName(), user.getLastName(), user.getAddress(), user.getZipCode(), user.getCity(), user.getBirthday(), user.getCreatedAt());

        if (result instanceof Long) {
            return ((Long) result).intValue();
        } else {
            return (Integer) result;
        }
    }

    @Override
    protected void close() throws Exception {
        connection.close();
    }
}
