# Performance of JDBC libraries

The idea of this test was taken from [Sql2o test](https://github.com/aaberg/sql2o/blob/master/core/src/test/java/org/sql2o/performance/PojoPerformanceTest.java) where they compare their library to other.
You can find their results [here](https://github.com/aaberg/sql2o/blob/master/README.md).

But, they used JBDI2 (current version is JDBI3) and many other libraries outdated, some missing.

## The features of this test
- use the latest versions of all libraries
- compare select and insert queries
- compare with bean mapping and without it (whether the method return User object or ResultSet, the implementation of this is very different between libraries)
- include open connection and close to the test rests
  (in original test, open and close not counted, but usual flow in your application will include this steps before and after (almost) each request)

## Libraries used in comparison
- [Apache DbUtils](https://commons.apache.org/proper/commons-dbutils/)
- [FluentJdbc](https://github.com/zsoltherpai/fluent-jdbc)
- [Spring JdbcTemplate](https://spring.io/guides/gs/relational-data-access/)
- [Sql2o](https://github.com/aaberg/sql2o)
- [Jdbi3](https://github.com/jdbi/jdbi)
- [MyBatis](https://github.com/mybatis/mybatis-3)
- [Hibernate](https://hibernate.org/)

## Results using H2 (in-memory) database
### Insert (1 000 entries)
| Library             | Bean mapping | Duration               |
|---------------------|--------------|------------------------|
| Apache DbUtils      | -            | 10ms                   |
| JavaNative          | -            | 11ms (10,00% slower)   |
| FluentJdbc          | -            | 12ms (20,00% slower)   |
| Spring JdbcTemplate | -            | 21ms (110,00% slower)  |
| FluentJdbc          | +            | 24ms (140,00% slower)  |
| Spring JdbcTemplate | +            | 25ms (150,00% slower)  |
| Sql2o               | +            | 29ms (190,00% slower)  |
| Jdbi3               | -            | 44ms (340,00% slower)  |
| MyBatis             | +            | 54ms (440,00% slower)  |
| Jdbi3               | +            | 58ms (480,00% slower)  |
| Hibernate           | +            | 76ms (660,00% slower)  |

### Select (10 000 entries)
| Library             | Bean mapping | Duration               |
|---------------------|--------------|------------------------|
| JavaNative          | -            | 49ms                   |
| Apache DbUtils      | -            | 51ms (4,08% slower)    |
| Apache DbUtils      | +            | 77ms (57,14% slower)   |
| Spring JdbcTemplate | -            | 83ms (69,39% slower)   |
| FluentJdbc          | +            | 103ms (110,20% slower) |
| FluentJdbc          | -            | 105ms (114,29% slower) |
| Sql2o               | +            | 113ms (130,61% slower) |
| Spring JdbcTemplate | +            | 166ms (238,78% slower) |
| Jdbi3               | -            | 185ms (277,55% slower) |
| MyBatis             | +            | 248ms (406,12% slower) |
| Jdbi3               | +            | 356ms (626,53% slower) |
| Hibernate           | +            | 358ms (630,61% slower) |

## Results using MariaDB database
### Insert (1 000 entries)
| Library             | Bean mapping | Duration               |
|---------------------|--------------|------------------------|
| JavaNative          | -            | 1354ms                 |
| Apache DbUtils      | -            | 1371ms (1,26% slower)  |
| FluentJdbc          | -            | 1377ms (1,70% slower)  |
| Spring JdbcTemplate | -            | 1391ms (2,73% slower)  |
| Sql2o               | +            | 1393ms (2,88% slower)  |
| FluentJdbc          | +            | 1415ms (4,51% slower)  |
| Jdbi3               | -            | 1467ms (8,35% slower)  |
| Hibernate           | +            | 1495ms (10,41% slower) |
| Jdbi3               | +            | 1503ms (11,00% slower) |
| Spring JdbcTemplate | +            | 1664ms (22,90% slower) |
| MyBatis             | +            | 1969ms (45,42% slower) |

### Select (10 000 entries)
| Library             | Bean mapping | Duration               |
|---------------------|--------------|------------------------|
| JavaNative          | -            | 844ms                  |
| Spring JdbcTemplate | -            | 899ms (6,52% slower)   |
| Sql2o               | +            | 906ms (7,35% slower)   |
| FluentJdbc          | +            | 932ms (10,43% slower)  |
| FluentJdbc          | -            | 934ms (10,66% slower)  |
| Spring JdbcTemplate | +            | 1015ms (20,26% slower) |
| Jdbi3               | -            | 1064ms (26,07% slower) |
| Jdbi3               | +            | 1064ms (26,07% slower) |
| Hibernate           | +            | 1725ms (104,38% slower)|
| Apache DbUtils      | -            | 2023ms (139,69% slower)|
| Apache DbUtils      | +            | 2074ms (145,73% slower)|
| MyBatis             | +            | 3165ms (275,00% slower)|

## License
MIT
