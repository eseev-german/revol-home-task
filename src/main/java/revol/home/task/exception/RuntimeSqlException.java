package revol.home.task.exception;

public class RuntimeSqlException extends RuntimeException {
    public RuntimeSqlException() {
    }

    public RuntimeSqlException(String message) {
        super(message);
    }

    public RuntimeSqlException(String message, Throwable cause) {
        super(message, cause);
    }
}
