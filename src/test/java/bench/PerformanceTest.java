package bench;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.github.astappiev.jdbcperf.PerformanceTestWrapper;
import com.github.astappiev.jdbcperf.entity.User;
import com.github.astappiev.jdbcperf.impl.ApacheDbUtils;
import com.github.astappiev.jdbcperf.impl.ApacheDbUtilsBean;
import com.github.astappiev.jdbcperf.impl.FluentJdbc;
import com.github.astappiev.jdbcperf.impl.FluentJdbcBean;
import com.github.astappiev.jdbcperf.impl.Hibernate;
import com.github.astappiev.jdbcperf.impl.JavaNative;
import com.github.astappiev.jdbcperf.impl.SpringJdbcTemplate;
import com.github.astappiev.jdbcperf.impl.SpringJdbcTemplateBean;
import com.github.astappiev.jdbcperf.impl.Jdbi3;
import com.github.astappiev.jdbcperf.impl.Jdbi3Bean;
import com.github.astappiev.jdbcperf.impl.MyBatis;
import com.github.astappiev.jdbcperf.impl.Sql2o;
import com.zaxxer.hikari.HikariDataSource;

public class PerformanceTest {
    private static final int ITERATIONS = 1000;
    private static final HikariDataSource dataSource = new HikariDataSource();

    @BeforeAll
    public static void setup() throws SQLException {
        // dataSource.setJdbcUrl("jdbc:h2:mem:test");
        dataSource.setJdbcUrl("jdbc:mysql://localhost/test");
        dataSource.setUsername("root");
        dataSource.setMaximumPoolSize(1);

        try (Connection connection = dataSource.getConnection()) {
            // connection.prepareStatement("DROP TABLE IF EXISTS user;").execute();
            connection.prepareStatement(User.CREATE_TABLE).executeUpdate();
        }
    }

    @Test
    public void select() throws Exception {
        int totalSelect = ITERATIONS * 10;

        System.out.println("Generating " + ITERATIONS + " fake records\n");
        List<User> users = new ArrayList<>();
        for (int idx = 0; idx < ITERATIONS; idx++) {
            users.add(User.fake());
        }

        System.out.println("Running iterations\n");

        PerformanceTestWrapper tests = new PerformanceTestWrapper(dataSource);
        tests.add(new JavaNative());
        tests.add(new Sql2o());
        tests.add(new Hibernate());
        tests.add(new Jdbi3());
        tests.add(new Jdbi3Bean());
        tests.add(new ApacheDbUtils());
        tests.add(new ApacheDbUtilsBean());
        tests.add(new MyBatis());
        tests.add(new SpringJdbcTemplate());
        tests.add(new SpringJdbcTemplateBean());
        tests.add(new FluentJdbc());
        tests.add(new FluentJdbcBean());

        // will not be counted, first round results pretty strange
        tests.prepare();
        tests.runInsert(users, false);
        tests.runSelect(totalSelect, false);

        // go on
        tests.prepare();
        tests.runInsert(users, true);
        tests.printResults("of inserting " + users.size() + " entries");

        tests.prepare();
        tests.runSelect(totalSelect, true);
        tests.printResults("of selecting " + totalSelect + " entries");
    }
}