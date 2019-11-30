package revol.home.task.db;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import revol.home.task.exception.WrongDataException;
import revol.home.task.model.Account;
import revol.home.task.model.MoneyTransfer;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PreparedStatementExecutor {
    private final static Logger LOG = LoggerFactory.getLogger(PreparedStatementExecutor.class);

    public void executeMoneyTransfer(MoneyTransfer transfer,
                                     PreparedStatement getAccountStatement,
                                     PreparedStatement accountUpdateStatement) throws SQLException {

        Account sourceAccount = executeGetAccountStatement(getAccountStatement, transfer.getSourceAccount());

        BigDecimal transferAmount = transfer.getAmount();
        if (sourceAccount.getBalance()
                         .compareTo(transferAmount) < 0) {
            LOG.warn("Attempt to make a transfer from an account with a lack of fund.");
            throw new WrongDataException(String.format("There are not enough money for transfer in account with id=[%d].", sourceAccount.getId()));
        }

        Account destinationAccount = executeGetAccountStatement(getAccountStatement, transfer.getDestinationAccount());

        Account updatedSourceAccount = getUpdatedSourceAccount(sourceAccount, transferAmount);
        Account updatedDestinationAccount = getUpdatedDestinationAccount(transferAmount, destinationAccount);

        executeAccountUpdateStatement(accountUpdateStatement, updatedSourceAccount, updatedDestinationAccount);
    }

    public void executeAccountUpdateStatement(PreparedStatement statement, Account... accounts) throws
            SQLException {
        for (Account account : accounts) {
            statement.setBigDecimal(1, account.getBalance());
            statement.setLong(2, account.getId());
            statement.executeUpdate();
        }
    }

    public Account executeGetAccountStatement(PreparedStatement statement, Long accountId) throws
            SQLException {
        statement.setLong(1, accountId);
        ResultSet resultSet = statement.executeQuery();
        if (!resultSet.next()) {
            String noAccountErrorMessage = String.format("No account with given id=[%d].", accountId);
            LOG.error(noAccountErrorMessage);
            throw new WrongDataException(noAccountErrorMessage);
        }
        return getAccount(resultSet);
    }

    public List<Account> executeGetAllAccountsStatement(PreparedStatement statement) throws SQLException {
        ResultSet result = statement.executeQuery();
        return getAccountList(result);
    }

    public Account executeGetAccountStatement(Long accountId, PreparedStatement statement) throws SQLException {
        statement.setLong(1, accountId);
        ResultSet resultSet = statement.executeQuery();
        if (resultSet.next()) {
            return getAccount(resultSet);
        } else {
            String noAccountErrorMessage = String.format("No account with given id=[%d].", accountId);
            LOG.error(noAccountErrorMessage);
            throw new WrongDataException(noAccountErrorMessage);
        }
    }

    public Account executeCreateAccountStatement(Account account, PreparedStatement statement) throws SQLException {
        statement.setBigDecimal(1, account.getBalance());
        statement.execute();
        ResultSet generatedKey = statement.getGeneratedKeys();
        if (generatedKey.next()) {
            long id = generatedKey.getLong(1);
            return Account.builder()
                          .id(id)
                          .balance(account.getBalance())
                          .build();
        }
        return account;
    }

    private Account getUpdatedDestinationAccount(BigDecimal transferAmount, Account destinationAccount) {
        return Account.builder()
                      .id(destinationAccount.getId())
                      .balance(transferAmount.add(destinationAccount.getBalance()))
                      .build();
    }

    private Account getUpdatedSourceAccount(Account sourceAccount, BigDecimal transferAmount) {
        return Account.builder()
                      .id(sourceAccount.getId())
                      .balance(sourceAccount.getBalance()
                                            .subtract(transferAmount))
                      .build();
    }

    private List<Account> getAccountList(ResultSet result) throws SQLException {
        List<Account> accountList = new ArrayList<>();
        while (result.next()) {
            accountList.add(getAccount(result));
        }
        return accountList;
    }

    private Account getAccount(ResultSet result) throws SQLException {
        return Account.builder()
                      .id(result.getLong("ID"))
                      .balance(result.getBigDecimal("BALANCE"))
                      .build();
    }

}
