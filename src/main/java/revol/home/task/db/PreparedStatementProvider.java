package revol.home.task.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class PreparedStatementProvider {
    public PreparedStatement retrieveAccountUpdateStatement(Connection connection) throws SQLException {
        return connection.prepareStatement(
                "UPDATE ACCOUNT SET BALANCE=? WHERE ID=?");
    }

    public PreparedStatement retrieveGetAccountStatement(Connection connection) throws SQLException {
        return connection.prepareStatement(
                "SELECT * FROM ACCOUNT WHERE ID=?");
    }

    public PreparedStatement retrieveCreateAccountStatement(Connection connection) throws SQLException {
        return connection.prepareStatement(
                "INSERT INTO ACCOUNT (BALANCE) VALUES (?)", Statement.RETURN_GENERATED_KEYS);
    }

    public PreparedStatement retrieveGetAllAccountsStatement(Connection connection) throws SQLException {
        return connection.prepareStatement("SELECT * FROM ACCOUNT");
    }
}
