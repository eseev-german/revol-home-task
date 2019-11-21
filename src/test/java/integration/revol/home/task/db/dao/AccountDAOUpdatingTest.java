package integration.revol.home.task.db.dao;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import revol.home.task.model.Account;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class AccountDAOUpdatingTest extends DaoBaseTest {
    private final static long FIRST_ACCOUNT_ID = 11L;
    private final static BigDecimal FIRST_ACCOUNT_BALANCE = BigDecimal.TEN;
    private final static long SECOND_ACCOUNT_ID = 3L;
    private final static BigDecimal SECOND_ACCOUNT_BALANCE = BigDecimal.ONE;

    private static final Account FIRST_ACCOUNT;
    private static final Account SECOND_ACCOUNT;

    static {
        FIRST_ACCOUNT = Account.builder()
                               .id(FIRST_ACCOUNT_ID)
                               .balance(FIRST_ACCOUNT_BALANCE)
                               .build();
        SECOND_ACCOUNT = Account.builder()
                                .id(SECOND_ACCOUNT_ID)
                                .balance(SECOND_ACCOUNT_BALANCE)
                                .build();
    }

    @Before
    public void setUp() throws Exception {
        createAccount(FIRST_ACCOUNT);
        createAccount(SECOND_ACCOUNT);
    }

    @After
    public void tearDown() throws Exception {
        deleteAllRows();
    }

    @Test
    public void updateOneAccount() throws SQLException {
        Account newAccount = Account.builder()
                                    .id(FIRST_ACCOUNT_ID)
                                    .balance(BigDecimal.ZERO)
                                    .build();

        ACCOUNT_DAO.updateAccounts(newAccount);

        Account updatedAccount = getAccountById(FIRST_ACCOUNT_ID);

        assertEquals(newAccount, updatedAccount);
    }

    @Test
    public void updateTwoAccounts() throws SQLException {
        Account firstNewAccount = Account.builder()
                                         .id(FIRST_ACCOUNT_ID)
                                         .balance(BigDecimal.ZERO)
                                         .build();
        Account secondNewAccount = Account.builder()
                                          .id(SECOND_ACCOUNT_ID)
                                          .balance(BigDecimal.TEN)
                                          .build();

        ACCOUNT_DAO.updateAccounts(firstNewAccount, secondNewAccount);

        List<Account> allAccounts = getAllAccounts();

        assertEquals(2, allAccounts.size());
        assertTrue(allAccounts.contains(firstNewAccount));
        assertTrue(allAccounts.contains(secondNewAccount));
    }

    @Test(expected = NullPointerException.class)
    public void npeWhenAccountsIsNull() {
        ACCOUNT_DAO.updateAccounts(null);
    }
}
