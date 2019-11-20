package integration;

import org.h2.jdbcx.JdbcConnectionPool;

import java.util.function.Supplier;

public class TestConnectionPoolProvider implements Supplier<JdbcConnectionPool> {
    private final static JdbcConnectionPool CONNECTION_POOL = JdbcConnectionPool.create(
            "jdbc:h2:mem:test;INIT=RUNSCRIPT FROM 'classpath:scripts/test_init.sql'", "", "");

    public JdbcConnectionPool get() {
        return CONNECTION_POOL;
    }
}