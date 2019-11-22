package revol.home.task.dto;

import java.util.Objects;

public class MoneyTransferDTO {
    private String sourceAccount;
    private String destinationAccount;
    private String amount;

    public String getSourceAccount() {
        return sourceAccount;
    }

    public void setSourceAccount(String sourceAccount) {
        this.sourceAccount = sourceAccount;
    }

    public String getDestinationAccount() {
        return destinationAccount;
    }

    public void setDestinationAccount(String destinationAccount) {
        this.destinationAccount = destinationAccount;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MoneyTransferDTO that = (MoneyTransferDTO) o;
        return Objects.equals(sourceAccount, that.sourceAccount) &&
                Objects.equals(destinationAccount, that.destinationAccount) &&
                Objects.equals(amount, that.amount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sourceAccount, destinationAccount, amount);
    }

    @Override
    public String toString() {
        return "MoneyTransferDTO{" +
                "sourceAccount='" + sourceAccount + '\'' +
                ", destinationAccount='" + destinationAccount + '\'' +
                ", amount='" + amount + '\'' +
                '}';
    }
}