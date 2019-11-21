package revol.home.task.db.dao;

import org.h2.jdbcx.JdbcConnectionPool;
import revol.home.task.db.ConnectionPoolProvider;
import revol.home.task.exception.RuntimeSqlException;
import revol.home.task.model.Account;

import javax.inject.Inject;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AccountDAO {

    private final ConnectionPoolProvider connectionPoolProvider;

    @Inject
    public AccountDAO(ConnectionPoolProvider connectionPoolProvider) {
        this.connectionPoolProvider = connectionPoolProvider;
    }

    public Account createAccount(Account account) {
        Objects.requireNonNull(account);
        JdbcConnectionPool connectionPool = connectionPoolProvider.get();
        try (Connection connection = connectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "INSERT INTO ACCOUNT (BALANCE) VALUES (?)", Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setBigDecimal(1, account.getBalance());
            preparedStatement.execute();
            ResultSet generatedKey = preparedStatement.getGeneratedKeys();
            if (generatedKey.next()) {
                long id = generatedKey.getLong(1);
                return Account.builder()
                              .id(id)
                              .balance(account.getBalance())
                              .build();
            }
        } catch (SQLException e) {
            throw new RuntimeSqlException("Problem while creating account is occurred.", e);
        }
        return account;
    }

    public Account getAccountById(Long transactionId) {
        Objects.requireNonNull(transactionId);
        JdbcConnectionPool connectionPool = connectionPoolProvider.get();
        try (Connection connection = connectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "SELECT * FROM ACCOUNT WHERE ID=? ")) {
            preparedStatement.setLong(1, transactionId);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            return getAccount(resultSet);
        } catch (SQLException e) {
            throw new RuntimeSqlException(String.format("Problem while getting account with id=[%d] is occurred.", transactionId), e);
        }
    }

    public List<Account> getAllAccounts() {
        JdbcConnectionPool connectionPool = connectionPoolProvider.get();
        try (Connection connection = connectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM ACCOUNT")) {

            ResultSet result = preparedStatement.executeQuery();
            return getAccountList(result);
        } catch (SQLException e) {
            throw new RuntimeSqlException("Problem while getting accounts is occurred.", e);
        }
    }


    public void updateAccounts(Account... accounts) {
        Objects.requireNonNull(accounts);
        JdbcConnectionPool connectionPool = connectionPoolProvider.get();
        try (Connection connection = connectionPool.getConnection();
             PreparedStatement accountUpdateStatement = connection.prepareStatement(
                     "UPDATE ACCOUNT SET BALANCE=? WHERE ID=?")) {
            try {
                connection.setAutoCommit(false);

                for (Account account : accounts) {
                    accountUpdateStatement.setBigDecimal(1, account.getBalance());
                    accountUpdateStatement.setLong(2, account.getId());
                    accountUpdateStatement.executeUpdate();
                }

                connection.commit();
            } catch (SQLException e) {
                connection.rollback();
                throw e;
            }
        } catch (SQLException e) {
            throw new RuntimeSqlException("Problem while updating accounts is occurred.", e);
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
