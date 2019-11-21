package revol.home.task.db;

import org.h2.jdbcx.JdbcConnectionPool;

import java.util.function.Supplier;

public interface ConnectionPoolProvider extends Supplier<JdbcConnectionPool> {
}