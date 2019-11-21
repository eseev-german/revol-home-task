package integration.revol.home.task.db.dao;

import org.junit.After;
import org.junit.Test;
import revol.home.task.model.Account;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

import static integration.revol.home.task.DaoTestUtil.ACCOUNT_DAO;
import static integration.revol.home.task.DaoTestUtil.deleteAllRows;
import static integration.revol.home.task.DaoTestUtil.getAllAccounts;
import static org.junit.Assert.assertEquals;

public class AccountDAOCreationTest {
    private final static Account ACCOUNT;

    static {
        ACCOUNT = Account.builder()
                         .balance(BigDecimal.TEN)
                         .build();
    }

    @After
    public void tearDown() throws Exception {
        deleteAllRows();
    }

    @Test
    public void returnedAccountWithGivenBalance() {
        Account result = ACCOUNT_DAO.createAccount(ACCOUNT);

        assertEquals(BigDecimal.TEN, result.getBalance());
    }

    @Test
    public void accountSuccessfullyCreated() throws SQLException {
        ACCOUNT_DAO.createAccount(ACCOUNT);

        List<Account> allAccounts = getAllAccounts();

        assertEquals(BigDecimal.TEN, allAccounts.get(0)
                                                .getBalance());
    }
}
