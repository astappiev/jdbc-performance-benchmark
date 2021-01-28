package com.github.astappiev.jdbcperf.impl;

import java.sql.SQLException;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;

import com.github.astappiev.jdbcperf.AbstractJdbcLib;
import com.github.astappiev.jdbcperf.entity.User;

public class Hibernate extends AbstractJdbcLib {
    private SessionFactory sessionFactory;
    private Session session;

    @Override
    public void init() throws SQLException {
        Configuration cfg = new Configuration();
        cfg.getProperties().put(Environment.DATASOURCE, dataSource);

        cfg.addAnnotatedClass(User.class);

        sessionFactory = cfg.buildSessionFactory();
    }

    @Override
    protected void open() throws Exception {
        session = sessionFactory.openSession();
    }

    @Override
    public User select(int id) {
        return session.get(User.class, id);
    }

    @Override
    protected Integer insert(final User user) throws Exception {
        return (Integer) session.save(user);
    }

    @Override
    protected void close() throws Exception {
        session.close();
    }
}
