package com.test.currency.domain;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "CLIENT")
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;

    @Column(name = "name")
    String name;

    @Column(name = "exclusive_plan")
    boolean exclusivePlan;

    @Column(name = "balance")
    BigDecimal balance;

    @Column(unique = true, name = "account_number")
    String accountNumber;

    @Column(name = "birth_date")
    Date birthDate;

    public Client() {
    }

    public Client(String name, boolean exclusivePlan, BigDecimal balance, String accountNumber, Date birthDate) {
        this.name = name;
        this.exclusivePlan = exclusivePlan;
        this.balance = balance;
        this.accountNumber = accountNumber;
        this.birthDate = birthDate;
    }

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
