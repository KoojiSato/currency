package com.test.currency.service;

import com.test.currency.CurrencyApplication;
import com.test.currency.TestUtils;
import com.test.currency.domain.Client;
import com.test.currency.domain.Transaction;
import com.test.currency.repository.ClientRepository;
import com.test.currency.repository.TransactionRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@SpringBootTest(classes = CurrencyApplication.class)
public class OperationServiceIT {

    @Autowired
    ClientRepository clientRepository;

    @Autowired
    OperationService operationService;

    @Autowired
    TransactionRepository transactionRepository;

    TestUtils testUtils;

    @BeforeEach
    void createClients(){
        testUtils = new TestUtils(clientRepository);
        testUtils.createClients();
    }

    @AfterEach
    void clearTest(){
        testUtils.clearClients();
        List<Transaction> list = transactionRepository.findAll();
        transactionRepository.deleteAll(list);
    }

    @Test
    public void testWithdrawExclusiveOperation(){
        Optional<Client> optClient = clientRepository.findByAccountNumber(TestUtils.EXCLUSIVE_ACCOUNT);
        BigDecimal balanceBefore = optClient.get().getBalance();
        BigDecimal balanceAfter = balanceBefore.subtract(BigDecimal.valueOf(200));
        operationService.withdrawOperation(BigDecimal.valueOf(200), TestUtils.EXCLUSIVE_ACCOUNT);
        optClient = clientRepository.findByAccountNumber(TestUtils.EXCLUSIVE_ACCOUNT);
        Assertions.assertEquals(optClient.get().getBalance(), balanceAfter);
    }

    @Test
    public void testWithdrawNormalOperationNoTax(){
        Optional<Client> optClient = clientRepository.findByAccountNumber(TestUtils.NORMAL_ACCOUNT);
        BigDecimal balanceBefore = optClient.get().getBalance();
        BigDecimal balanceAfter = balanceBefore.subtract(BigDecimal.valueOf(100));
        operationService.withdrawOperation(BigDecimal.valueOf(100), TestUtils.NORMAL_ACCOUNT);
        optClient = clientRepository.findByAccountNumber(TestUtils.NORMAL_ACCOUNT);
        Assertions.assertEquals(optClient.get().getBalance(), balanceAfter);
    }

    @Test
    public void testWithdrawNormalOperationFirstTax(){
        Optional<Client> optClient = clientRepository.findByAccountNumber(TestUtils.NORMAL_ACCOUNT);
        BigDecimal balanceBefore = optClient.get().getBalance();
        BigDecimal withdrawAmount = BigDecimal.valueOf(200);
        BigDecimal withTax = withdrawAmount.add(withdrawAmount.multiply(BigDecimal.valueOf(0.004))).setScale(2);
        BigDecimal balanceAfter = balanceBefore.subtract(withTax);
        operationService.withdrawOperation(withdrawAmount, TestUtils.NORMAL_ACCOUNT);
        optClient = clientRepository.findByAccountNumber(TestUtils.NORMAL_ACCOUNT);
        Assertions.assertEquals(optClient.get().getBalance(), balanceAfter);
    }

    @Test
    public void testWithdrawNormalOperationSecondTax(){
        Optional<Client> optClient = clientRepository.findByAccountNumber(TestUtils.NORMAL_ACCOUNT);
        BigDecimal balanceBefore = optClient.get().getBalance();
        BigDecimal withdrawAmount = BigDecimal.valueOf(400);
        BigDecimal withTax = withdrawAmount.add(withdrawAmount.multiply(BigDecimal.valueOf(0.01))).setScale(2);
        BigDecimal balanceAfter = balanceBefore.subtract(withTax);
        operationService.withdrawOperation(withdrawAmount, TestUtils.NORMAL_ACCOUNT);
        optClient = clientRepository.findByAccountNumber(TestUtils.NORMAL_ACCOUNT);
        Assertions.assertEquals(optClient.get().getBalance(), balanceAfter);
    }

    @Test
    public void testWithdrawExclusiveOperationNoFund(){
        Optional<Client> optClient = clientRepository.findByAccountNumber(TestUtils.NO_FUND_ACCOUNT);
        String message = "";
        try{
            operationService.withdrawOperation(BigDecimal.valueOf(200), TestUtils.NO_FUND_ACCOUNT);
        } catch (RuntimeException e){
            message = e.getMessage();
        }
        Assertions.assertEquals(message, "not enough funds");
    }

}
