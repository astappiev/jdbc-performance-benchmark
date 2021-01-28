package com.github.astappiev.jdbcperf.impl;

import org.codejargon.fluentjdbc.api.FluentJdbcBuilder;
import org.codejargon.fluentjdbc.api.mapper.Mappers;
import org.codejargon.fluentjdbc.api.mapper.ObjectMappers;
import org.codejargon.fluentjdbc.api.query.Query;
import org.codejargon.fluentjdbc.api.query.UpdateResultGenKeys;

import com.github.astappiev.jdbcperf.AbstractJdbcLib;
import com.github.astappiev.jdbcperf.entity.User;

public class FluentJdbcBean extends AbstractJdbcLib {
    private Query query;
    private ObjectMappers objectMappers;

    @Override
    public void init() {
        org.codejargon.fluentjdbc.api.FluentJdbc fluentJdbc = new FluentJdbcBuilder()
            .connectionProvider(dataSource)
            .build();

        query = fluentJdbc.query();

        objectMappers = ObjectMappers.builder().build();
    }

    @Override
    protected void open() throws Exception {
    }

    @Override
    public User select(int id) {
        return query.select("SELECT * FROM user WHERE id = :id").namedParam("id", id).firstResult(objectMappers.forClass(User.class)).orElse(null);
    }

    @Override
    protected Integer insert(final User user) throws Exception {
        UpdateResultGenKeys<Integer> result = query
            .update("insert into user (first_name, last_name, address, zip_code, city, birthday, created_at) "
                + "values (:firstName, :lastName, :address, :zipCode, :city, :birthday, :createdAt)")
            .namedParam("firstName", user.getFirstName())
            .namedParam("lastName", user.getLastName())
            .namedParam("lastName", user.getLastName())
            .namedParam("address", user.getAddress())
            .namedParam("zipCode", user.getZipCode())
            .namedParam("city", user.getCity())
            .namedParam("birthday", user.getBirthday())
            .namedParam("createdAt", user.getCreatedAt())
            .runFetchGenKeys(Mappers.singleInteger());

        return result.generatedKeys().get(0);
    }

    @Override
    protected void close() throws Exception {
    }
}
