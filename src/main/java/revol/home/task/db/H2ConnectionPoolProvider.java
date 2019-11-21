package revol.home.task.db;

import org.h2.jdbcx.JdbcConnectionPool;

public class H2ConnectionPoolProvider implements ConnectionPoolProvider {
    private final static JdbcConnectionPool CONNECTION_POOL = JdbcConnectionPool.create(
            "jdbc:h2:mem:test;INIT=RUNSCRIPT FROM 'classpath:scripts/init.sql'", "", "");

    public JdbcConnectionPool get() {
        return CONNECTION_POOL;
    }
}