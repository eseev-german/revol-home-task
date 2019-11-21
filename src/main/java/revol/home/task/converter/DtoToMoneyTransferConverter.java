package revol.home.task.converter;

import revol.home.task.dto.MoneyTransferDTO;
import revol.home.task.model.MoneyTransfer;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.function.Function;

public class DtoToMoneyTransferConverter implements Function<MoneyTransferDTO, MoneyTransfer> {

    @Override
    public MoneyTransfer apply(MoneyTransferDTO moneyTransferDTO) {
        Objects.requireNonNull(moneyTransferDTO);
        return MoneyTransfer.builder()
                            .amount(new BigDecimal(moneyTransferDTO.getAmount()))
                            .destinationAccount(Long.parseLong(moneyTransferDTO.getDestinationAccount()))
                            .sourceAccount(Long.parseLong(moneyTransferDTO.getSourceAccount()))
                            .build();
    }
}
