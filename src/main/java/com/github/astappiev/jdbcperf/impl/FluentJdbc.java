package com.github.astappiev.jdbcperf.impl;

import org.codejargon.fluentjdbc.api.FluentJdbcBuilder;
import org.codejargon.fluentjdbc.api.mapper.Mappers;
import org.codejargon.fluentjdbc.api.query.Query;
import org.codejargon.fluentjdbc.api.query.UpdateResultGenKeys;

import com.github.astappiev.jdbcperf.AbstractJdbcLib;
import com.github.astappiev.jdbcperf.entity.User;

public class FluentJdbc extends AbstractJdbcLib {
    private Query query;

    @Override
    public void init() {
        org.codejargon.fluentjdbc.api.FluentJdbc fluentJdbc = new FluentJdbcBuilder()
            .connectionProvider(dataSource)
            .build();

        query = fluentJdbc.query();
    }

    @Override
    protected void open() throws Exception {
    }

    @Override
    public User select(int id) {
        return query.select("SELECT * FROM user WHERE id = :id").namedParam("id", id).firstResult(rs -> {
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
        }).orElse(null);
    }

    @Override
    protected Integer insert(final User user) throws Exception {
        UpdateResultGenKeys<Integer> result = query
            .update("insert into user (first_name, last_name, address, zip_code, city, birthday, created_at) values (?, ?, ?, ?, ?, ?, ?)")
            .params(user.getFirstName(), user.getLastName(), user.getAddress(), user.getZipCode(), user.getCity(), user.getBirthday(), user.getCreatedAt())
            .runFetchGenKeys(Mappers.singleInteger());

        return result.generatedKeys().get(0);
    }

    @Override
    protected void close() throws Exception {
    }
}
