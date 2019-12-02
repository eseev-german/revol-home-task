package integration;

import org.h2.jdbcx.JdbcConnectionPool;
import revol.home.task.db.ConnectionPoolProvider;

import javax.ws.rs.ext.Provider;
import java.util.function.Supplier;

@Provider
public class TestConnectionPoolProvider implements ConnectionPoolProvider {
    private final static JdbcConnectionPool CONNECTION_POOL;

    static {
        CONNECTION_POOL = JdbcConnectionPool.create(
                "jdbc:h2:mem:test;INIT=RUNSCRIPT FROM 'classpath:scripts/test_init.sql'", "", "");
        CONNECTION_POOL.setMaxConnections(1000);
    }
    public JdbcConnectionPool get() {
        return CONNECTION_POOL;
    }
}