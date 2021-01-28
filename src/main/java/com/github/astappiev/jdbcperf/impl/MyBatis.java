package com.github.astappiev.jdbcperf.impl;

import java.sql.SQLException;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.TransactionFactory;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;

import com.github.astappiev.jdbcperf.AbstractJdbcLib;
import com.github.astappiev.jdbcperf.entity.User;

public class MyBatis extends AbstractJdbcLib {
    private SqlSessionFactory sessionFactory;
    private SqlSession session;

    @Override
    public void init() throws SQLException {
        TransactionFactory transactionFactory = new JdbcTransactionFactory();
        Environment environment = new Environment("development", transactionFactory, dataSource);
        org.apache.ibatis.session.Configuration config = new org.apache.ibatis.session.Configuration(environment);
        config.addMapper(MyBatisUserMapper.class);
        sessionFactory = new SqlSessionFactoryBuilder().build(config);
    }

    @Override
    protected void open() throws Exception {
        session = sessionFactory.openSession();
    }

    @Override
    public User select(int id) {
        return session.getMapper(MyBatisUserMapper.class).selectUser(id);
    }

    @Override
    protected Integer insert(final User user) throws Exception {
        session.getMapper(MyBatisUserMapper.class).insertAuthor(user);
        return user.getId();
    }

    @Override
    protected void close() throws Exception {
        session.close();
    }

    /**
     * Mapper interface required for MyBatis performance test
     */
    private interface MyBatisUserMapper {
        @Select("SELECT * FROM user WHERE id = #{id}")
        @Results({
            @Result(property = "firstName", column = "first_name"),
            @Result(property = "lastName", column = "last_name"),
            @Result(property = "zipCode", column = "zip_code"),
            @Result(property = "createdAt", column = "created_at")
        })
        User selectUser(int id);

        @Insert("insert into user (first_name, last_name, address, zip_code, city, birthday, created_at) values (#{firstName}, #{lastName}, #{address}, #{zipCode}, #{city}, #{birthday}, #{createdAt})")
        @Options(useGeneratedKeys = true, keyProperty = "id")
        int insertAuthor(User user);
    }
}
