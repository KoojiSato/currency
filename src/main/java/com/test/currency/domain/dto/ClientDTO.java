package com.test.currency.domain.dto;

import jakarta.persistence.Column;

import java.math.BigDecimal;
import java.util.Date;

public class ClientDTO {

    Long id;

    String name;

    boolean exclusivePlan;

    BigDecimal balance;

    String accountNumber;

    Date birthDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isExclusivePlan() {
        return exclusivePlan;
    }

    public void setExclusivePlan(boolean exclusivePlan) {
        this.exclusivePlan = exclusivePlan;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }
}
