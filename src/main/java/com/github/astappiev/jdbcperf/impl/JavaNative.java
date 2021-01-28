package com.github.astappiev.jdbcperf.impl;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;

import com.github.astappiev.jdbcperf.AbstractJdbcLib;
import com.github.astappiev.jdbcperf.entity.User;

public class JavaNative extends AbstractJdbcLib {
    private Connection connection;

    @Override
    protected void open() throws Exception {
        connection = dataSource.getConnection();
    }

    @Override
    public User select(int id) throws SQLException {
        try (PreparedStatement stmt = connection.prepareStatement("SELECT * FROM user WHERE id = ?")) {
            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
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

        return null;
    }

    @Override
    protected Integer insert(final User user) throws Exception {
        try (PreparedStatement stmt = connection.prepareStatement("insert into user (first_name, last_name, address, zip_code, city, birthday, created_at) values (?, ?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, user.getFirstName());
            stmt.setString(2, user.getLastName());
            if (user.getAddress() == null) {
                stmt.setNull(3, Types.VARCHAR);
            } else {
                stmt.setString(3, user.getAddress());
            }
            if (user.getZipCode() == null) {
                stmt.setNull(4, Types.INTEGER);
            } else {
                stmt.setInt(4, user.getZipCode());
            }
            if (user.getCity() == null) {
                stmt.setNull(5, Types.VARCHAR);
            } else {
                stmt.setString(5, user.getCity());
            }
            if (user.getBirthday() == null) {
                stmt.setNull(6, Types.DATE);
            } else {
                stmt.setDate(6, new Date(user.getBirthday().getTime()));
            }

            stmt.setTimestamp(7, new Timestamp(user.getCreatedAt().getTime()));

            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }
        }

        return null;
    }

    @Override
    protected void close() throws Exception {
        connection.close();
    }
}
