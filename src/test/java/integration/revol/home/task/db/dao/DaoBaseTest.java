package integration.revol.home.task.db.dao;

import integration.TestConnectionPoolProvider;
import org.h2.jdbcx.JdbcConnectionPool;
import revol.home.task.db.ConnectionPoolProvider;
import revol.home.task.db.dao.AccountDAO;
import revol.home.task.model.Account;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DaoBaseTest {
    private final static ConnectionPoolProvider POOL_PROVIDER = new TestConnectionPoolProvider();

    protected final static AccountDAO ACCOUNT_DAO = new AccountDAO(POOL_PROVIDER);

    protected void createAccount(Account account) throws SQLException {
        try (Connection connection = POOL_PROVIDER.get()
                                                  .getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "INSERT INTO ACCOUNT (ID, BALANCE) VALUES (?, ?)", Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setLong(1, account.getId());
            preparedStatement.setBigDecimal(2, account.getBalance());
            preparedStatement.execute();
        }
    }

    protected void deleteAllRows() throws SQLException {
        try (Connection connection = POOL_PROVIDER.get()
                                                  .getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM ACCOUNT")) {
            preparedStatement.executeUpdate();
        }
    }

    protected Account getAccountById(Long id) throws SQLException {
        JdbcConnectionPool connectionPool = POOL_PROVIDER.get();
        try (Connection connection = connectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM ACCOUNT WHERE ID=?")) {
            preparedStatement.setLong(1, id);
            ResultSet result = preparedStatement.executeQuery();
            result.next();
            return getAccount(result);
        }
    }

    protected List<Account> getAllAccounts() throws SQLException {
        JdbcConnectionPool connectionPool = POOL_PROVIDER.get();
        try (Connection connection = connectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM ACCOUNT")) {
            ResultSet result = preparedStatement.executeQuery();
            return getAccountList(result);
        }
    }

    private List<Account> getAccountList(ResultSet result) throws SQLException {
        List<Account> accountList = new ArrayList<>();
        while (result.next()) {
            accountList.add(getAccount(result));
        }
        return accountList;
    }

    private Account getAccount(ResultSet resultSet) throws SQLException {
        return Account.builder()
                      .id(resultSet.getLong("ID"))
                      .balance(resultSet.getBigDecimal("BALANCE"))
                      .build();
    }
}
