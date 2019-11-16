package revol.home.task.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AccountDTO {
    @JsonProperty(access=JsonProperty.Access.READ_ONLY)
    private String id;
    private String balance;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }
}