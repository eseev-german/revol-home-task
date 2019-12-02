package revol.home.task.exception;

public class RuntimeDaoException extends RuntimeException {
    public RuntimeDaoException() {
    }

    public RuntimeDaoException(String message) {
        super(message);
    }

    public RuntimeDaoException(String message, Throwable cause) {
        super(message, cause);
    }
}
