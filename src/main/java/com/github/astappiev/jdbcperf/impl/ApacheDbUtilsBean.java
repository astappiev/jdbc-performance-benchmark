package com.github.astappiev.jdbcperf.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

import org.apache.commons.dbutils.BasicRowProcessor;
import org.apache.commons.dbutils.BeanProcessor;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import com.github.astappiev.jdbcperf.AbstractJdbcLib;
import com.github.astappiev.jdbcperf.entity.User;

public class ApacheDbUtilsBean extends AbstractJdbcLib {
    private QueryRunner run;
    private ResultSetHandler<User> rsHandler;

    private Connection connection;

    @Override
    public void init() {
        run = new QueryRunner();
        rsHandler = new BeanHandler<>(User.class, new BasicRowProcessor(new BeanProcessor(Map.of(
            "FIRST_NAME", "firstName",
            "LAST_NAME", "lastName",
            "ZIP_CODE", "zipCode",
            "CREATED_AT", "createdAt"
        ))));
    }

    @Override
    protected void open() throws Exception {
        connection = dataSource.getConnection();
    }

    @Override
    public User select(int id) throws SQLException {
        return run.query(connection, "SELECT * FROM user WHERE id = ?", rsHandler, id);
    }

    @Override
    protected Integer insert(final User user) throws Exception {
        Object result = run.insert(connection, "insert into user (first_name, last_name, address, zip_code, city, birthday, created_at) values (?, ?, ?, ?, ?, ?, ?)", new ScalarHandler<Long>(),
            user.getFirstName(), user.getLastName(), user.getAddress(), user.getZipCode(), user.getCity(), user.getBirthday(), user.getCreatedAt());

        // pretty strange but using H2 and MariaDB it returns different type
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
