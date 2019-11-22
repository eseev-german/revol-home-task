package revol.home.task.model;

import java.math.BigDecimal;
import java.util.Objects;

public final class Account {
    private final Long id;
    private final BigDecimal balance;


    private Account(Builder builder) {
        this.id = builder.id;
        this.balance = builder.balance;
    }

    public static Builder builder() {
        return new Builder();
    }


    public Long getId() {
        return id;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public static final class Builder {
        private Long id;
        private BigDecimal balance;

        private Builder() {
        }


        public Account build() {
            return new Account(this);
        }

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder balance(BigDecimal balance) {
            this.balance = balance;
            return this;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return Objects.equals(id, account.id) &&
                Objects.equals(balance, account.balance);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, balance);
    }

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", balance=" + balance +
                '}';
    }
}