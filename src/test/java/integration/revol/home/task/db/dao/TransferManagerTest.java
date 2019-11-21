package integration.revol.home.task.db.dao;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import revol.home.task.converter.DtoToMoneyTransferConverter;
import revol.home.task.dto.MoneyTransferDTO;
import revol.home.task.exception.TransferException;
import revol.home.task.manager.TransferManager;
import revol.home.task.model.Account;

import java.math.BigDecimal;
import java.sql.SQLException;

import static org.junit.Assert.assertEquals;

public class TransferManagerTest extends DaoBaseTest {
    private final static Account EMPTY_ACCOUNT;
    private final static Account ACCOUNT_WITH_MONEY;
    private final static String EMPTY_ACCOUNT_ID_STRING = "10";
    private final static long EMPTY_ACCOUNT_ID = 10L;
    private final static String ACCOUNT_WITH_MONEY_ID_STRING = "2";
    private final static long ACCOUNT_WITH_MONEY_ID = 2L;

    private TransferManager transferManager;

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
        transferManager = new TransferManager(ACCOUNT_DAO, new DtoToMoneyTransferConverter());
        createAccount(EMPTY_ACCOUNT);
        createAccount(ACCOUNT_WITH_MONEY);
    }

    @After
    public void tearDown() throws Exception {
        deleteAllRows();
    }

    @Test
    public void successfulTransfer() throws SQLException {
        MoneyTransferDTO transfer = new MoneyTransferDTO();
        transfer.setAmount("1");
        transfer.setSourceAccount(ACCOUNT_WITH_MONEY_ID_STRING);
        transfer.setDestinationAccount(EMPTY_ACCOUNT_ID_STRING);

        transferManager.transferMoney(transfer);

        Account sourceAccount = getAccountById(ACCOUNT_WITH_MONEY_ID);
        Account destinationAccount = getAccountById(EMPTY_ACCOUNT_ID);

        assertEquals(BigDecimal.valueOf(9L), sourceAccount.getBalance());
        assertEquals(BigDecimal.ONE, destinationAccount.getBalance());
    }

    @Test(expected = TransferException.class)
    public void notEnoughMoneyOnSourceAccount() {
        MoneyTransferDTO transfer = new MoneyTransferDTO();
        transfer.setAmount("1");
        transfer.setSourceAccount(EMPTY_ACCOUNT_ID_STRING);
        transfer.setDestinationAccount(ACCOUNT_WITH_MONEY_ID_STRING);

        transferManager.transferMoney(transfer);
    }

    @Test(expected = TransferException.class)
    public void transferringValueIsNegative() {
        MoneyTransferDTO transfer = new MoneyTransferDTO();
        transfer.setAmount("-11");
        transfer.setSourceAccount(ACCOUNT_WITH_MONEY_ID_STRING);
        transfer.setDestinationAccount(EMPTY_ACCOUNT_ID_STRING);

        transferManager.transferMoney(transfer);
    }
}