package com.github.astappiev.jdbcperf;

import javax.sql.DataSource;

import org.apache.commons.lang3.time.StopWatch;

import com.github.astappiev.jdbcperf.entity.User;

public abstract class AbstractJdbcLib {
    protected final StopWatch watch = new StopWatch();
    protected DataSource dataSource;

    public String getName() {
        return getClass().getSimpleName();
    }

    protected void init() throws Exception {
        // nothing
    }

    protected abstract void open() throws Exception;

    protected abstract User select(final int id) throws Exception;

    protected abstract Integer insert(final User user) throws Exception;

    protected abstract void close() throws Exception;
}
