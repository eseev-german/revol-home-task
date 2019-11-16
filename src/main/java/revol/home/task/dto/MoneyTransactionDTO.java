package revol.home.task.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MoneyTransactionDTO {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String timestamp;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String sourceAccount;
    private String destinationAccount;
    private String amount;

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

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
}