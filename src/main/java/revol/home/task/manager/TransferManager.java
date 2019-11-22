package revol.home.task.manager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import revol.home.task.converter.DtoToMoneyTransferConverter;
import revol.home.task.db.dao.AccountDAO;
import revol.home.task.dto.MoneyTransferDTO;
import revol.home.task.exception.WrongDataException;
import revol.home.task.model.Account;
import revol.home.task.model.MoneyTransfer;

import javax.inject.Inject;
import java.math.BigDecimal;

public class TransferManager {
    private final static Logger LOG = LoggerFactory.getLogger(TransferManager.class);

    private final AccountDAO accountDAO;
    private final DtoToMoneyTransferConverter dtoToMoneyTransferConverter;

    @Inject
    public TransferManager(AccountDAO accountDAO, DtoToMoneyTransferConverter dtoToMoneyTransferConverter) {
        this.accountDAO = accountDAO;
        this.dtoToMoneyTransferConverter = dtoToMoneyTransferConverter;
    }

    public void transferMoney(MoneyTransferDTO transferDTO) {
        MoneyTransfer transfer = getConvertedMoneyTransfer(transferDTO);

        Account sourceAccount = accountDAO.getAccountById(transfer.getSourceAccount());
        BigDecimal transferAmount = transfer.getAmount();
        if (sourceAccount.getBalance()
                         .compareTo(transferAmount) < 0) {
            LOG.warn("Attempt to make a transfer from an account with a lack of fund.");
            throw new WrongDataException(String.format("There are not enough money for transfer in account with id=[%d].", sourceAccount.getId()));
        }

        Account destinationAccount = accountDAO.getAccountById(transfer.getDestinationAccount());

        Account updatedSourceAccount = getUpdatedSourceAccount(sourceAccount, transferAmount);

        Account updatedDestinationAccount = getUpdatedDestinationAccount(transferAmount, destinationAccount);
        accountDAO.updateAccounts(updatedSourceAccount, updatedDestinationAccount);
    }

    private MoneyTransfer getConvertedMoneyTransfer(MoneyTransferDTO transferDTO) {
        MoneyTransfer transfer = dtoToMoneyTransferConverter.apply(transferDTO);
        if (transfer.getAmount()
                    .signum() < 0) {
            LOG.warn("Attempt to make a transfer of negative value.");
            throw new WrongDataException("It is impossible to transfer negative value.");
        }
        return transfer;
    }

    private Account getUpdatedDestinationAccount(BigDecimal transferAmount, Account destinationAccount) {
        return Account.builder()
                      .id(destinationAccount.getId())
                      .balance(transferAmount.add(destinationAccount.getBalance()))
                      .build();
    }

    private Account getUpdatedSourceAccount(Account sourceAccount, BigDecimal transferAmount) {
        return Account.builder()
                      .id(sourceAccount.getId())
                      .balance(sourceAccount.getBalance()
                                            .subtract(transferAmount))
                      .build();
    }
}