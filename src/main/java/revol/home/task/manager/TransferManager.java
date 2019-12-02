package revol.home.task.manager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import revol.home.task.converter.DtoToMoneyTransferConverter;
import revol.home.task.db.dao.AccountDAO;
import revol.home.task.dto.MoneyTransferDTO;
import revol.home.task.exception.WrongDataException;
import revol.home.task.model.MoneyTransfer;

import javax.inject.Inject;

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
        accountDAO.transferMoney(getConvertedMoneyTransfer(transferDTO));
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
}