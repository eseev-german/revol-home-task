package revol.home.task.converter;

import org.junit.Before;
import org.junit.Test;
import revol.home.task.dto.MoneyTransferDTO;
import revol.home.task.exception.WrongDataException;
import revol.home.task.model.MoneyTransfer;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;

public class DtoToMoneyTransferConverterTest {

    private DtoToMoneyTransferConverter converter;

    @Before
    public void setUp() {
        converter = new DtoToMoneyTransferConverter();
    }

    @Test(expected = WrongDataException.class)
    public void moneyTransferDtoIsNull() {
        converter.apply(null);
    }

    @Test(expected = WrongDataException.class)
    public void wrongAmountValue() {
        MoneyTransferDTO moneyTransferDTO = new MoneyTransferDTO();
        moneyTransferDTO.setAmount("wrong_balance");
        moneyTransferDTO.setDestinationAccount("1");
        moneyTransferDTO.setSourceAccount("2");

        converter.apply(moneyTransferDTO);
    }

    @Test(expected = WrongDataException.class)
    public void wrongDestinationAccount() {
        MoneyTransferDTO moneyTransferDTO = new MoneyTransferDTO();
        moneyTransferDTO.setAmount("10");
        moneyTransferDTO.setDestinationAccount("Wrong_destination_account");
        moneyTransferDTO.setSourceAccount("2");

        converter.apply(moneyTransferDTO);
    }

    @Test(expected = WrongDataException.class)
    public void wrongSourceAccount() {
        MoneyTransferDTO moneyTransferDTO = new MoneyTransferDTO();
        moneyTransferDTO.setAmount("10");
        moneyTransferDTO.setDestinationAccount("1");
        moneyTransferDTO.setSourceAccount("Wrong_source_account");

        converter.apply(moneyTransferDTO);
    }

    @Test
    public void accountDtoIgnoreId() {
        MoneyTransferDTO accountDTO = new MoneyTransferDTO();
        accountDTO.setAmount("10");
        accountDTO.setSourceAccount("1");
        accountDTO.setDestinationAccount("2");

        MoneyTransfer result = converter.apply(accountDTO);

        assertEquals(BigDecimal.TEN, result.getAmount());
        assertEquals(Long.valueOf(1), result.getSourceAccount());
        assertEquals(Long.valueOf(2), result.getDestinationAccount());
    }
}