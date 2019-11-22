package revol.home.task.converter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import revol.home.task.dto.AccountDTO;
import revol.home.task.exception.WrongDataException;
import revol.home.task.model.Account;

import java.math.BigDecimal;
import java.util.function.Function;

public class DtoToAccountConverter implements Function<AccountDTO, Account> {
    private final static Logger LOG = LoggerFactory.getLogger(DtoToAccountConverter.class);

    @Override
    public Account apply(AccountDTO accountDTO) {
        return Account.builder()
                      .balance(getBalanceAsBigDecimal(accountDTO))
                      .build();
    }

    private BigDecimal getBalanceAsBigDecimal(AccountDTO accountDTO) {
        try {
            return new BigDecimal(accountDTO.getBalance());
        } catch (NullPointerException | NumberFormatException e) {
            LOG.warn(String.format("Exception while transform account dto: %s; Error message: %s", accountDTO, e.getMessage()));
            throw new WrongDataException("Wrong account data.", e);
        }
    }
}