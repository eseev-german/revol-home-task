package revol.home.task.manager;

import revol.home.task.converter.AccountToDtoConverter;
import revol.home.task.converter.DtoToAccountConverter;
import revol.home.task.db.dao.AccountDAO;
import revol.home.task.dto.AccountDTO;

import javax.inject.Inject;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class AccountManager {
    private final AccountDAO accountDAO;
    private final AccountToDtoConverter accountToDtoConverter;
    private final DtoToAccountConverter dtoToAccountConverter;

    @Inject
    public AccountManager(AccountDAO accountDAO,
                          AccountToDtoConverter accountToDtoConverter,
                          DtoToAccountConverter dtoToAccountConverter) {
        this.accountDAO = accountDAO;
        this.accountToDtoConverter = accountToDtoConverter;
        this.dtoToAccountConverter = dtoToAccountConverter;
    }

    public AccountDTO createAccount(AccountDTO account) {
        Objects.requireNonNull(account, "Account cannot be null.");
        return dtoToAccountConverter.andThen(accountDAO::createAccount)
                                    .andThen(accountToDtoConverter)
                                    .apply(account);
    }

    public AccountDTO getAccount(String accountId) {
        Objects.requireNonNull(accountId, "Account id cannot be null.");
        return accountToDtoConverter.compose(accountDAO::getAccountById)
                                    .apply(Long.parseLong(accountId));
    }

    public List<AccountDTO> getAccounts() {
        return accountDAO.getAllAccounts()
                         .stream()
                         .map(accountToDtoConverter)
                         .collect(Collectors.toList());
    }
}