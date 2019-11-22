package integration.revol.home.task.manager;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import revol.home.task.converter.AccountToDtoConverter;
import revol.home.task.converter.DtoToAccountConverter;
import revol.home.task.dto.AccountDTO;
import revol.home.task.exception.WrongDataException;
import revol.home.task.manager.AccountManager;
import revol.home.task.model.Account;

import java.math.BigDecimal;
import java.sql.SQLException;

import static integration.revol.home.task.DaoTestUtil.ACCOUNT_DAO;
import static integration.revol.home.task.DaoTestUtil.deleteAllRows;
import static integration.revol.home.task.DaoTestUtil.getAccountById;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

public class AccountManagerCreatingTest {

    private AccountManager accountManager;

    @Before
    public void setUp() throws Exception {
        accountManager = new AccountManager(ACCOUNT_DAO, new AccountToDtoConverter(), new DtoToAccountConverter());
    }

    @After
    public void tearDown() throws Exception {
        deleteAllRows();
    }

    @Test
    public void successfullyCreateAccountWithIgnoredGivenId() throws SQLException {
        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setBalance("10");
        String id = "100";
        accountDTO.setId(id);

        AccountDTO result = accountManager.createAccount(accountDTO);

        assertNotEquals(id, result.getId());
        Account accountInDb = getAccountById(result.getId());

        assertEquals(BigDecimal.TEN, accountInDb.getBalance());
    }

    @Test
    public void successfullyCreateAccountWithoutGivenId() throws SQLException {
        AccountDTO accountDTO = new AccountDTO();
        String balance = "10";
        accountDTO.setBalance(balance);

        AccountDTO result = accountManager.createAccount(accountDTO);

        assertEquals(balance, result.getBalance());
        assertNotNull(result.getId());

        Account accountInDb = getAccountById(result.getId());

        assertEquals(BigDecimal.TEN, accountInDb.getBalance());
    }

    @Test(expected = WrongDataException.class)
    public void createEmptyAccount() {
        AccountDTO accountDTO = new AccountDTO();

        accountManager.createAccount(accountDTO);
    }

    @Test(expected = WrongDataException.class)
    public void createNullAccount() {
        accountManager.createAccount(null);
    }
}