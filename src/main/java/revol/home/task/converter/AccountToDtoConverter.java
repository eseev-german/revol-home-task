package revol.home.task.converter;

import revol.home.task.dto.AccountDTO;
import revol.home.task.model.Account;

import java.util.Objects;
import java.util.function.Function;

public class AccountToDtoConverter implements Function<Account, AccountDTO> {

    @Override
    public AccountDTO apply(Account account) {
        Objects.requireNonNull(account, "Account cannot be null.");
        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setId(Objects.toString(account.getId(), null));
        accountDTO.setBalance(Objects.toString(account.getBalance(), null));
        return accountDTO;
    }
}
