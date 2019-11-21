package integration.revol.home.task.db.dao;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import revol.home.task.model.Account;
import revol.home.task.model.MoneyTransfer;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class AccountDAOTransferMoneyTest extends AccountDaoBaseTest {

    private final static Account EMPTY_ACCOUNT;
    private final static Account ACCOUNT_WITH_MONEY;
    private final static long EMPTY_ACCOUNT_ID = 10L;
    private final static long ACCOUNT_WITH_MONEY_ID = 2L;

    static {
        EMPTY_ACCOUNT = Account.builder()
                               .id(EMPTY_ACCOUNT_ID)
                               .balance(BigDecimal.ZERO)
                               .build();

        ACCOUNT_WITH_MONEY = Account.builder()
                                    .id(ACCOUNT_WITH_MONEY_ID)
                                    .balance(BigDecimal.TEN)
                                    .build();
    }

    @Before
    public void setUp() throws Exception {
        createAccount(EMPTY_ACCOUNT);
        createAccount(ACCOUNT_WITH_MONEY);
    }

    @After
    public void tearDown() throws Exception {
        deleteAllRows();
    }

    @Test
    public void successfulTransfer() throws SQLException {
        MoneyTransfer transfer = MoneyTransfer.builder()
                                              .sourceAccount(ACCOUNT_WITH_MONEY_ID)
                                              .destinationAccount(EMPTY_ACCOUNT_ID)
                                              .amount(BigDecimal.ONE)
                                              .build();

        ACCOUNT_DAO.transferMoney(transfer);

        List<Account> allAccounts = getAllAccounts();
        Account sourceAccount = allAccounts.stream()
                                           .filter(a -> ACCOUNT_WITH_MONEY_ID == a.getId())
                                           .findAny()
                                           .get();
        Account destinationAccount = allAccounts.stream()
                                                .filter(a -> EMPTY_ACCOUNT_ID == a.getId())
                                                .findAny()
                                                .get();

        assertEquals(2, allAccounts.size());
        assertEquals(BigDecimal.valueOf(9L), sourceAccount.getBalance());
        assertEquals(BigDecimal.ONE, destinationAccount.getBalance());
    }

    @Test(expected = RuntimeException.class)
    public void notEnoughMoneyOnSourceAccount() {
        MoneyTransfer transfer = MoneyTransfer.builder()
                                              .sourceAccount(EMPTY_ACCOUNT_ID)
                                              .destinationAccount(ACCOUNT_WITH_MONEY_ID)
                                              .amount(BigDecimal.ONE)
                                              .build();

        ACCOUNT_DAO.transferMoney(transfer);
    }

    @Test(expected = RuntimeException.class)
    public void transferringValueIsNegative() {
        MoneyTransfer transfer = MoneyTransfer.builder()
                                              .sourceAccount(ACCOUNT_WITH_MONEY_ID)
                                              .destinationAccount(EMPTY_ACCOUNT_ID)
                                              .amount(BigDecimal.valueOf(-1))
                                              .build();

        ACCOUNT_DAO.transferMoney(transfer);
    }
}
