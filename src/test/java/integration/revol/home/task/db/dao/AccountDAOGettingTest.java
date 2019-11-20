package integration.revol.home.task.db.dao;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;
import revol.home.task.model.Account;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(BlockJUnit4ClassRunner.class)
public class AccountDAOGettingTest extends AccountDaoBaseTest {
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
    public void getRightAccount() {

        Account result = ACCOUNT_DAO.getAccountById(SECOND_ACCOUNT_ID);
        assertEquals(SECOND_ACCOUNT, result);
    }

    @Test
    public void getAllSavedAccounts() throws SQLException {
        List<Account> result = ACCOUNT_DAO.getAllAccounts();

        assertEquals(2, result.size());
        assertEquals(FIRST_ACCOUNT, result.get(result.indexOf(FIRST_ACCOUNT)));
        assertEquals(SECOND_ACCOUNT, result.get(result.indexOf(SECOND_ACCOUNT)));
    }

    @Test(expected = NullPointerException.class)
    public void npeWhenAccountIsNull() {
        ACCOUNT_DAO.createAccount(null);
    }
}
