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
import java.util.List;

import static integration.revol.home.task.DaoTestUtil.ACCOUNT_DAO;
import static integration.revol.home.task.DaoTestUtil.createAccount;
import static integration.revol.home.task.DaoTestUtil.deleteAllRows;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class AccountManagerGettingTest {

    private final static Account FIRST_ACCOUNT;
    private final static Account SECOND_ACCOUNT;
    private final static AccountDTO FIRST_EXPECTED_ACCOUNT_DTO;
    private final static AccountDTO SECOND_EXPECTED_ACCOUNT_DTO;
    private final static String FIRST_ACCOUNT_ID_STRING = "10";
    private final static long FIRST_ACCOUNT_ID = 10L;
    private final static String SECOND_ACCOUNT_ID_STRING = "2";
    private final static long SECOND_ACCOUNT_ID = 2L;

    private AccountManager accountManager;

    static {
        FIRST_ACCOUNT = Account.builder()
                               .id(FIRST_ACCOUNT_ID)
                               .balance(BigDecimal.ZERO)
                               .build();
        FIRST_EXPECTED_ACCOUNT_DTO = new AccountDTO();
        FIRST_EXPECTED_ACCOUNT_DTO.setId(FIRST_ACCOUNT_ID_STRING);
        FIRST_EXPECTED_ACCOUNT_DTO.setBalance("0");

        SECOND_ACCOUNT = Account.builder()
                                .id(SECOND_ACCOUNT_ID)
                                .balance(BigDecimal.TEN)
                                .build();
        SECOND_EXPECTED_ACCOUNT_DTO = new AccountDTO();
        SECOND_EXPECTED_ACCOUNT_DTO.setId(SECOND_ACCOUNT_ID_STRING);
        SECOND_EXPECTED_ACCOUNT_DTO.setBalance("10");

    }

    @Before
    public void setUp() throws Exception {
        accountManager = new AccountManager(ACCOUNT_DAO, new AccountToDtoConverter(), new DtoToAccountConverter());
        createAccount(FIRST_ACCOUNT);
        createAccount(SECOND_ACCOUNT);
    }

    @After
    public void tearDown() throws Exception {
        deleteAllRows();
    }

    @Test
    public void successfullyGetAllAccounts() {
        List<AccountDTO> result = accountManager.getAccounts();

        assertEquals(2, result.size());
        assertTrue(result.contains(FIRST_EXPECTED_ACCOUNT_DTO));
        assertTrue(result.contains(SECOND_EXPECTED_ACCOUNT_DTO));
    }

    @Test
    public void successfullyGetSecondAccount() {
        AccountDTO result = accountManager.getAccount(SECOND_ACCOUNT_ID_STRING);

        assertEquals(result, SECOND_EXPECTED_ACCOUNT_DTO);
    }

    @Test(expected = WrongDataException.class)
    public void getAccountByEmptyId() {
        accountManager.getAccount(null);
    }

    @Test(expected = WrongDataException.class)
    public void getAccountByWrongId() {
        accountManager.getAccount("Not_a_long");
    }

    @Test(expected = WrongDataException.class)
    public void getAccountByUnknownId() {
        accountManager.getAccount("1");
    }
}