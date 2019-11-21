package revol.home.task.converter;

import org.junit.Before;
import org.junit.Test;
import revol.home.task.dto.AccountDTO;
import revol.home.task.model.Account;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class DtoToAccountConverterTest {

    private DtoToAccountConverter converter;

    @Before
    public void setUp() {
        converter = new DtoToAccountConverter();
    }

    @Test(expected = NullPointerException.class)
    public void accountDtoIsNull() {
        converter.apply(null);
    }

    @Test(expected = NumberFormatException.class)
    public void wrongBalanceValue() {
        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setBalance("wrong_balance");

        converter.apply(accountDTO);
    }

    @Test
    public void accountDtoIgnoreId() {
        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setId("Id");
        accountDTO.setBalance("10");

        Account result = converter.apply(accountDTO);
        assertNull(result.getId());
        assertEquals(BigDecimal.TEN, result.getBalance());
    }
}