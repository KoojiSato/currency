package com.test.currency.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.test.currency.CurrencyApplication;
import com.test.currency.TestUtils;
import com.test.currency.domain.Client;
import com.test.currency.domain.Transaction;
import com.test.currency.domain.dto.OperationDTO;
import com.test.currency.repository.ClientRepository;
import com.test.currency.repository.TransactionRepository;
import org.h2.util.json.JSONString;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.format.datetime.DateFormatter;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = CurrencyApplication.class)
@AutoConfigureMockMvc
public class OperationResourceIT {

    @Autowired
    ClientRepository clientRepository;

    @Autowired
    TransactionRepository transactionRepository;

    TestUtils testUtils;

    @Autowired
    MockMvc mvc;

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
    public void testPostWithdrawNormal() throws Exception {
        BigDecimal hundred = BigDecimal.valueOf(100);
        Optional<Client> optClient = clientRepository.findByAccountNumber(TestUtils.NORMAL_ACCOUNT);
        BigDecimal balanceBefore = optClient.get().getBalance();
        BigDecimal balanceAfter = balanceBefore.subtract(hundred);
        String json = createOperationJson(TestUtils.NORMAL_ACCOUNT, hundred);
        mvc.perform(MockMvcRequestBuilders.post("/api/withdraw").content(json).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
        optClient = clientRepository.findByAccountNumber(TestUtils.NORMAL_ACCOUNT);
        Assertions.assertEquals(balanceAfter, optClient.get().getBalance());
    }

    @Test
    public void testPostWithdrawNoFund() throws Exception {
        BigDecimal hundred = BigDecimal.valueOf(100);
        Optional<Client> optClient = clientRepository.findByAccountNumber(TestUtils.NO_FUND_ACCOUNT);
        String json = createOperationJson(TestUtils.NO_FUND_ACCOUNT, hundred);
        mvc.perform(MockMvcRequestBuilders.post("/api/withdraw").content(json).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andExpect(status().is5xxServerError());
    }

    @Test
    public void testPostDeposity() throws Exception {
        BigDecimal hundred = BigDecimal.valueOf(100);
        Optional<Client> optClient = clientRepository.findByAccountNumber(TestUtils.NORMAL_ACCOUNT);
        BigDecimal balanceBefore = optClient.get().getBalance();
        BigDecimal balanceAfter = balanceBefore.add(hundred);
        String json = createOperationJson(TestUtils.NORMAL_ACCOUNT, hundred);
        mvc.perform(MockMvcRequestBuilders.post("/api/deposity").content(json).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
        optClient = clientRepository.findByAccountNumber(TestUtils.NORMAL_ACCOUNT);
        Assertions.assertEquals(balanceAfter, optClient.get().getBalance());
    }

    @Test
    public void testTransactions() throws Exception {
        Optional<Client> optClient = clientRepository.findByAccountNumber(TestUtils.NORMAL_ACCOUNT);
        BigDecimal twoHundred = BigDecimal.valueOf(200).setScale(1);
        String deposityOperation = createOperationJson(TestUtils.NORMAL_ACCOUNT, twoHundred);
        mvc.perform(MockMvcRequestBuilders.post("/api/deposity").content(deposityOperation).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
        BigDecimal hundred = BigDecimal.valueOf(100).setScale(1);
        String withdrawOperation = createOperationJson(TestUtils.NORMAL_ACCOUNT, hundred);
        mvc.perform(MockMvcRequestBuilders.post("/api/withdraw").content(withdrawOperation).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
        Date now = Date.from(Instant.now());
        DateFormatter formatter = new DateFormatter("dd/MM/yyyy");
        mvc.perform(MockMvcRequestBuilders.get(String.format("/api/operations?transactionDate=" + formatter.print(now, Locale.getDefault())+ "&accountNumber=" + TestUtils.NORMAL_ACCOUNT)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.[0].amount").value(twoHundred))
                .andExpect(jsonPath("$.[0].withdraw").value(false))
                .andExpect(jsonPath("$.[0].clientId").value(optClient.get().getId()))
                .andExpect(jsonPath("$.[1].amount").value(hundred))
                .andExpect(jsonPath("$.[1].withdraw").value(true))
                .andExpect(jsonPath("$.[1].clientId").value(optClient.get().getId()));
    }


    private static String createOperationJson(String normalAccount, BigDecimal hundred) throws JsonProcessingException {
        OperationDTO operationDTO = new OperationDTO();
        operationDTO.setAccountNumber(normalAccount);
        operationDTO.setValue(hundred);
        return TestUtils.toJson(operationDTO);
    }

}
