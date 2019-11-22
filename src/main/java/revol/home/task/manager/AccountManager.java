package revol.home.task.manager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import revol.home.task.converter.AccountToDtoConverter;
import revol.home.task.converter.DtoToAccountConverter;
import revol.home.task.db.dao.AccountDAO;
import revol.home.task.dto.AccountDTO;
import revol.home.task.exception.WrongDataException;

import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;

public class AccountManager {
    private final static Logger LOG = LoggerFactory.getLogger(AccountManager.class);

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
        return dtoToAccountConverter.andThen(accountDAO::createAccount)
                                    .andThen(accountToDtoConverter)
                                    .apply(account);
    }

    public AccountDTO getAccount(String accountId) {
        return accountToDtoConverter.compose(accountDAO::getAccountById)
                                    .compose(this::accountIdAsLong)
                                    .apply(accountId);
    }

    private Long accountIdAsLong(String id) {
        try {
            return Long.parseLong(id);
        } catch (NumberFormatException e) {
            String errorMessage = String.format("Wrong account id=[%s]", id);
            LOG.warn(errorMessage);
            throw new WrongDataException(errorMessage);
        }
    }

    public List<AccountDTO> getAccounts() {
        return accountDAO.getAllAccounts()
                         .stream()
                         .map(accountToDtoConverter)
                         .collect(Collectors.toList());
    }
}