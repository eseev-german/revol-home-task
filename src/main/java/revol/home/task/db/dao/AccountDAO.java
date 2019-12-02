package revol.home.task.db.dao;

import org.h2.jdbcx.JdbcConnectionPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import revol.home.task.db.ConnectionPoolProvider;
import revol.home.task.db.PreparedStatementExecutor;
import revol.home.task.db.PreparedStatementProvider;
import revol.home.task.exception.RuntimeDaoException;
import revol.home.task.exception.WrongDataException;
import revol.home.task.model.Account;
import revol.home.task.model.MoneyTransfer;

import javax.inject.Inject;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class AccountDAO {
    private final static Logger LOG = LoggerFactory.getLogger(AccountDAO.class);

    private final ConnectionPoolProvider connectionPoolProvider;
    private final PreparedStatementProvider statementProvider;
    private final PreparedStatementExecutor statementExecutor;
    private final Lock readLock;
    private final Lock writeLock;

    {
        ReadWriteLock lock = new ReentrantReadWriteLock(true);
        readLock = lock.readLock();
        writeLock = lock.writeLock();
    }

    @Inject
    public AccountDAO(ConnectionPoolProvider connectionPoolProvider,
                      PreparedStatementProvider statementProvider,
                      PreparedStatementExecutor statementExecutor) {
        this.connectionPoolProvider = connectionPoolProvider;
        this.statementProvider = statementProvider;
        this.statementExecutor = statementExecutor;
    }

    public Account createAccount(Account account) {
        Objects.requireNonNull(account);
        JdbcConnectionPool connectionPool = connectionPoolProvider.get();
        try (Connection connection = connectionPool.getConnection();
             PreparedStatement createAccountStatement = statementProvider.retrieveCreateAccountStatement(connection)) {
            return runInWriteLock(() -> statementExecutor.executeCreateAccountStatement(account, createAccountStatement));
        } catch (Exception e) {
            String errorMessage = "Problem while creating account is occurred.";
            LOG.error(errorMessage, e);
            throw new RuntimeDaoException(errorMessage, e);
        }
    }

    public Account getAccountById(Long accountId) {
        Objects.requireNonNull(accountId);
        JdbcConnectionPool connectionPool = connectionPoolProvider.get();
        try (Connection connection = connectionPool.getConnection();
             PreparedStatement getAccountStatement = statementProvider.retrieveGetAccountStatement(connection)) {
            return runInReadLock(() -> statementExecutor.executeGetAccountStatement(accountId, getAccountStatement));
        } catch (WrongDataException e) {
            throw e;
        } catch (Exception e) {
            String errorMessage = String.format("Problem while getting account with id=[%d] is occurred.", accountId);
            LOG.error(errorMessage, e);
            throw new RuntimeDaoException(errorMessage, e);
        }
    }

    public List<Account> getAllAccounts() {
        JdbcConnectionPool connectionPool = connectionPoolProvider.get();
        try (Connection connection = connectionPool.getConnection();
             PreparedStatement allAccountsStatement = statementProvider.retrieveGetAllAccountsStatement(connection)) {
            return runInReadLock(() -> statementExecutor.executeGetAllAccountsStatement(allAccountsStatement));
        } catch (Exception e) {
            String errorMessage = "Problem while getting accounts is occurred.";
            LOG.error(errorMessage, e);
            throw new RuntimeDaoException(errorMessage, e);
        }
    }

    public void transferMoney(MoneyTransfer transfer) {
        JdbcConnectionPool connectionPool = connectionPoolProvider.get();
        try (Connection connection = connectionPool.getConnection();
             PreparedStatement getAccountStatement = statementProvider.retrieveGetAccountStatement(connection);
             PreparedStatement accountUpdateStatement = statementProvider.retrieveAccountUpdateStatement(connection)) {
            runInWriteLock(() -> doMoneyTransferWithinTransaction(transfer, connection, getAccountStatement, accountUpdateStatement));
        } catch (WrongDataException e) {
            throw e;
        } catch (Exception e) {
            String errorMessage = "Problem while updating accounts is occurred.";
            LOG.error(errorMessage, e);
            throw new RuntimeDaoException(errorMessage, e);
        }
    }

    private <T> T runInWriteLock(Callable<T> task) throws Exception {
        writeLock.lock();
        try {
            return task.call();
        } finally {
            writeLock.unlock();
        }
    }

    private <T> T runInReadLock(Callable<T> task) throws Exception {
        readLock.lock();
        try {
            return task.call();
        } finally {
            readLock.unlock();
        }
    }

    private Connection doMoneyTransferWithinTransaction(MoneyTransfer transfer, Connection connection,
                                                        PreparedStatement getAccountPreparedStatement,
                                                        PreparedStatement accountUpdateStatement) throws Exception {
        try {
            connection.setAutoCommit(false);
            statementExecutor.executeMoneyTransfer(transfer, getAccountPreparedStatement, accountUpdateStatement);
            connection.commit();
            return connection;
        } catch (WrongDataException e) {
            throw e;
        } catch (Exception e) {
            connection.rollback();
            throw e;
        }
    }
}