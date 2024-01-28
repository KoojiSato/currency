package com.test.currency.domain.dto;

import java.io.Serializable;
import java.math.BigDecimal;

public class OperationDTO implements Serializable {

    private String accountNumber;

    private BigDecimal value;

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }
}
