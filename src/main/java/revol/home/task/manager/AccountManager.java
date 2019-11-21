package revol.home.task.manager;

import revol.home.task.converter.AccountToDtoConverter;
import revol.home.task.converter.DtoToAccountConverter;
import revol.home.task.converter.DtoToMoneyTransferConverter;
import revol.home.task.db.ConnectionPoolProvider;
import revol.home.task.db.dao.AccountDAO;
import revol.home.task.dto.AccountDTO;
import revol.home.task.dto.MoneyTransferDTO;

import java.util.List;
import java.util.stream.Collectors;

public class AccountManager {
    private AccountDAO accountDAO;
    private AccountToDtoConverter accountToDtoConverter;
    private DtoToAccountConverter dtoToAccountConverter;
    private DtoToMoneyTransferConverter dtoToMoneyTransferConverter;

    public AccountManager() {
        accountDAO = new AccountDAO(new ConnectionPoolProvider());
        accountToDtoConverter = new AccountToDtoConverter();
        dtoToAccountConverter = new DtoToAccountConverter();
        dtoToMoneyTransferConverter = new DtoToMoneyTransferConverter();
    }

    public void transferMoney(MoneyTransferDTO transaction) {
        accountDAO.transferMoney(dtoToMoneyTransferConverter.apply(transaction));
    }

    public AccountDTO createAccount(AccountDTO account) {
        return dtoToAccountConverter.andThen(accountDAO::createAccount)
                                    .andThen(accountToDtoConverter)
                                    .apply(account);
    }

    public AccountDTO getAccount(String accountId) {
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