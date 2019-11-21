package revol.home.task.converter;

import revol.home.task.dto.AccountDTO;
import revol.home.task.model.Account;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.function.Function;

public class DtoToAccountConverter implements Function<AccountDTO, Account> {

    @Override
    public Account apply(AccountDTO accountDTO) {
        Objects.requireNonNull(accountDTO);
        return Account.builder()
                      .balance(new BigDecimal(accountDTO.getBalance()))
                      .build();
    }
}