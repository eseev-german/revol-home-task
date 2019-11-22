package revol.home.task.converter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import revol.home.task.dto.AccountDTO;
import revol.home.task.exception.WrongDataException;
import revol.home.task.model.Account;

import java.util.Objects;
import java.util.function.Function;

public class AccountToDtoConverter implements Function<Account, AccountDTO> {
    private final static Logger LOG = LoggerFactory.getLogger(AccountToDtoConverter.class);

    @Override
    public AccountDTO apply(Account account) {
        if (account == null) {
            LOG.warn("Account is null");
            throw new WrongDataException("Account cannot be null.");
        }
        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setId(Objects.toString(account.getId(), null));
        accountDTO.setBalance(Objects.toString(account.getBalance(), null));
        return accountDTO;
    }
}
