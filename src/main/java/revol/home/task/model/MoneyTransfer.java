package revol.home.task.model;

import java.math.BigDecimal;

public final class MoneyTransfer {
    private final Long sourceAccount;
    private final Long destinationAccount;
    private final BigDecimal amount;

    private MoneyTransfer(Builder builder) {
        this.sourceAccount = builder.sourceAccount;
        this.destinationAccount = builder.destinationAccount;
        this.amount = builder.amount;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static Builder builder(MoneyTransfer moneyTransaction) {
        return new Builder();
    }

    public Long getSourceAccount() {
        return sourceAccount;
    }

    public Long getDestinationAccount() {
        return destinationAccount;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public static final class Builder {
        private Long sourceAccount;
        private Long destinationAccount;
        private BigDecimal amount;

        private Builder() {
        }

        private Builder(MoneyTransfer moneyTransaction) {
            this.sourceAccount = moneyTransaction.getSourceAccount();
            this.destinationAccount = moneyTransaction.getDestinationAccount();
            this.amount = moneyTransaction.getAmount();
        }

        public MoneyTransfer build() {
            return new MoneyTransfer(this);
        }


        public Builder sourceAccount(Long sourceAccount) {
            this.sourceAccount = sourceAccount;
            return this;
        }

        public Builder destinationAccount(Long destinationAccount) {
            this.destinationAccount = destinationAccount;
            return this;
        }

        public Builder amount(BigDecimal amount) {
            this.amount = amount;
            return this;
        }
    }
}