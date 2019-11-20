package revol.home.task.db.dao;

import org.h2.jdbcx.JdbcConnectionPool;
import revol.home.task.model.Account;
import revol.home.task.model.MoneyTransfer;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

public class AccountDAO {

    private final Supplier<JdbcConnectionPool> connectionPoolProvider;

    public AccountDAO(Supplier<JdbcConnectionPool> connectionPoolProvider) {
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
            throw new RuntimeException(e);
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
            throw new RuntimeException(e);
        }
    }

    public List<Account> getAllAccounts() {
        JdbcConnectionPool connectionPool = connectionPoolProvider.get();
        try (Connection connection = connectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM ACCOUNT")) {

            ResultSet result = preparedStatement.executeQuery();
            return getAccountList(result);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void transferMoney(MoneyTransfer transfer) {
        Objects.requireNonNull(transfer);

        if (transfer.getAmount()
                    .signum() < 0) {
            throw new RuntimeException("It is impossible to transfer negative value");
        }

        Account sourceAccount = getAccountById(transfer.getSourceAccount());
        BigDecimal transferAmount = transfer.getAmount();
        if (sourceAccount.getBalance()
                         .compareTo(transferAmount) < 0) {
            throw new RuntimeException("There are not enough money for transfer in account with id=" + sourceAccount.getId());
        }
        Account destinationAccount = getAccountById(transfer.getDestinationAccount());

        Account updatedSourceAccount = Account.builder()
                                              .id(sourceAccount.getId())
                                              .balance(sourceAccount.getBalance().subtract(transferAmount))
                                              .build();

        Account updatedDestinationAccount = Account.builder()
                                                   .id(destinationAccount.getId())
                                                   .balance(transferAmount.add(destinationAccount.getBalance()))
                                                   .build();

        updateAccount(updatedSourceAccount, updatedDestinationAccount);
    }

    private void updateAccount(Account firstAccount, Account secondAccount) {
        JdbcConnectionPool connectionPool = connectionPoolProvider.get();
        try (Connection connection = connectionPool.getConnection();
             PreparedStatement accountUpdateStatement = connection.prepareStatement(
                     "UPDATE ACCOUNT SET BALANCE=? WHERE ID=?")) {
            connection.setAutoCommit(false);

            accountUpdateStatement.setBigDecimal(1, firstAccount.getBalance());
            accountUpdateStatement.setLong(2, firstAccount.getId());
            accountUpdateStatement.executeUpdate();

            accountUpdateStatement.setBigDecimal(1, secondAccount.getBalance());
            accountUpdateStatement.setLong(2, secondAccount.getId());
            accountUpdateStatement.executeUpdate();

            connection.commit();
        } catch (SQLException e) {
            throw new RuntimeException(e);
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
