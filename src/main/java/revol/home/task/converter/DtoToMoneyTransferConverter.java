package revol.home.task.converter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import revol.home.task.dto.MoneyTransferDTO;
import revol.home.task.exception.WrongDataException;
import revol.home.task.model.MoneyTransfer;

import java.math.BigDecimal;
import java.util.function.Function;
import java.util.function.Supplier;

public class DtoToMoneyTransferConverter implements Function<MoneyTransferDTO, MoneyTransfer> {
    private final static Logger LOG = LoggerFactory.getLogger(DtoToMoneyTransferConverter.class);

    @Override
    public MoneyTransfer apply(MoneyTransferDTO moneyTransferDTO) {
        return MoneyTransfer.builder()
                            .amount(getAmountAsBigDecimal(moneyTransferDTO))
                            .destinationAccount(getIdAsLong(moneyTransferDTO.getDestinationAccount()))
                            .sourceAccount(getIdAsLong(moneyTransferDTO.getSourceAccount()))
                            .build();
    }

    private long getIdAsLong(String id) {
        return getParsedValue(() -> Long.parseLong(id));
    }

    private BigDecimal getAmountAsBigDecimal(MoneyTransferDTO moneyTransferDTO) {
        return getParsedValue(() -> new BigDecimal(moneyTransferDTO.getAmount()));
    }

    private <T> T getParsedValue(Supplier<T> provider) {
        try {
            return provider.get();
        } catch (NumberFormatException | NullPointerException e) {
            LOG.warn(String.format("Exception while transform money transfer dto; Error message: %s", e.getMessage()));
            throw new WrongDataException("Wrong money transfer data.", e);
        }
    }
}
