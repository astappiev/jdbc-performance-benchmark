package com.github.astappiev.jdbcperf;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.sql.DataSource;

import com.github.astappiev.jdbcperf.entity.User;

public class PerformanceTestWrapper extends ArrayList<AbstractJdbcLib> {
    private DataSource dataSource;

    public PerformanceTestWrapper(DataSource ds) {
        dataSource = ds;
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public void prepare() throws Exception {
        for (AbstractJdbcLib test : this) {
            test.dataSource = dataSource;
            test.init();

            test.watch.reset();
            test.watch.start();
            test.watch.suspend();
        }
    }

    public void runSelect(int iterations, boolean includeConnection) throws Exception {
        for (int i = 1; i <= iterations; i++) {
            Collections.shuffle(this);
            for (AbstractJdbcLib test : this) {
                try {
                    User user;
                    if (includeConnection) {
                        test.watch.resume();
                        test.open(); // prepare connection
                        user = test.select(i);
                        test.close(); // close connection
                        test.watch.suspend();
                    } else {
                        test.open(); // prepare connection
                        test.watch.resume();
                        user = test.select(i);
                        test.watch.suspend();
                        test.close(); // close connection
                    }

                    // System.out.println(test.getName() + ": " + user);
                } catch (Exception e) {
                    System.out.println("An error happened " + e);
                    e.printStackTrace();
                }
            }
        }
    }

    public void runInsert(List<User> users, boolean includeConnection) throws Exception {
        for (User user : users) {
            Collections.shuffle(this);
            for (AbstractJdbcLib test : this) {
                try {
                    Integer userId;
                    if (includeConnection) {
                        test.watch.resume();
                        test.open(); // prepare connection
                        userId = test.insert(user);
                        test.close(); // close connection
                        test.watch.suspend();
                    } else {
                        test.open(); // prepare connection
                        test.watch.resume();
                        userId = test.insert(user);
                        test.watch.suspend();
                        test.close(); // close connection
                    }

                    // System.out.println(test.getName() + ": " + userId);
                } catch (Exception e) {
                    System.out.println("An error happened " + e);
                    e.printStackTrace();
                }
            }
        }
    }

    public void printResults(String title) {
        this.sort(Comparator.comparingLong(value -> value.watch.getTime()));

        System.out.println("Results " + title);
        System.out.println("-------------------------");

        AbstractJdbcLib fastest = null;
        for (AbstractJdbcLib test : this) {
            long millis = test.watch.getTime();

            if (fastest == null) {
                fastest = test;
                System.out.printf("%s took %dms%n", test.getName(), millis);
            } else {
                long fastestMillis = fastest.watch.getTime();
                double percentSlower = (double) (millis - fastestMillis) / fastestMillis * 100;
                System.out.printf("%s took %dms (%.2f%% slower)%n", test.getName(), millis, percentSlower);
            }
        }
    }
}
