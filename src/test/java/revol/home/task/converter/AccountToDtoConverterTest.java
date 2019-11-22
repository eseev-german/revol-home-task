package revol.home.task.converter;

import org.junit.Before;
import org.junit.Test;
import revol.home.task.dto.AccountDTO;
import revol.home.task.exception.WrongDataException;
import revol.home.task.model.Account;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class AccountToDtoConverterTest {

    private AccountToDtoConverter converter;

    @Before
    public void setUp() {
        converter = new AccountToDtoConverter();
    }

    @Test(expected = WrongDataException.class)
    public void accountIsNull() {
        converter.apply(null);
    }

    @Test
    public void accountWithEmptyId() {
        Account accountWithEmptyId = Account.builder()
                                            .balance(BigDecimal.TEN)
                                            .build();

        AccountDTO result = converter.apply(accountWithEmptyId);

        assertEquals("10", result.getBalance());
        assertNull(result.getId());
    }

    @Test
    public void accountWithEmptyBalance() {
        Account accountWithEmptyId = Account.builder()
                                            .id(1L)
                                            .build();

        AccountDTO result = converter.apply(accountWithEmptyId);

        assertEquals("1", result.getId());
        assertNull(result.getBalance());
    }
}