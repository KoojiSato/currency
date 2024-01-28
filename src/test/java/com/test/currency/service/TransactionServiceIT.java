package com.test.currency.service;

import com.test.currency.CurrencyApplication;
import com.test.currency.TestUtils;
import com.test.currency.domain.Client;
import com.test.currency.domain.Transaction;
import com.test.currency.domain.dto.TransactionDTO;
import com.test.currency.repository.ClientRepository;
import com.test.currency.repository.TransactionRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

@SpringBootTest(classes = CurrencyApplication.class)
public class TransactionServiceIT {

    private static final BigDecimal HUNDRED = BigDecimal.valueOf(100);
    private static final BigDecimal TWO_HUNDRED = BigDecimal.valueOf(200);

    @Autowired
    ClientRepository clientRepository;

    @Autowired
    TransactionService transactionService;

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
    public void testCreateNewTransaction(){
        Client client = testUtils.findFirst();
        transactionService.createNewTransaction(HUNDRED, client.getId(), false);
        List<TransactionDTO> list = transactionService.findAll();
        Assertions.assertEquals(list.size(), 1);
        TransactionDTO transactionDTO = list.get(0);
        Assertions.assertEquals(transactionDTO.getAmount().compareTo(HUNDRED), 0);
        Assertions.assertEquals(transactionDTO.getClientId(), client.getId());
        Assertions.assertEquals(transactionDTO.isWithdraw(), false);
    }

    @Test
    public void testFindByDateClient(){
        List<Client> clients = clientRepository.findAll();
        Client clientOne = clients.get(0);
        Client clientTwo = clients.get(1);
        Date now = Date.from(Instant.now());
        createTransactions(clientOne, now);
        createTransactions(clientTwo, now);
        List<TransactionDTO> transactions = transactionService.findByDateClient(now, clientOne.getId());
        Assertions.assertEquals(transactions.size(), 2);
        for(TransactionDTO dto : transactions){
            verifyTransaction(clientOne, now, dto);
        }
    }

    @Test
    public void testFindByDate(){
        List<Client> clients = clientRepository.findAll();
        Client clientOne = clients.get(0);
        Client clientTwo = clients.get(1);
        Date now = Date.from(Instant.now());
        createTransactions(clientOne, now);
        createTransactions(clientTwo, now);
        List<TransactionDTO> transactions = transactionService.findByDate(now);
        Assertions.assertEquals(transactions.size(), 4);
        for(TransactionDTO dto : transactions){
            if(dto.getClientId().equals(clientOne.getId())) {
                verifyTransaction(clientOne, now, dto);
            }else{
                verifyTransaction(clientTwo, now, dto);
            }
        }

    }

    private static void verifyTransaction(Client client, Date now, TransactionDTO dto) {
        if(dto.isWithdraw()){
            Assertions.assertEquals(dto.getAmount(), HUNDRED.setScale(2));
        }else{
            Assertions.assertEquals(dto.getAmount(), TWO_HUNDRED.setScale(2));
        }
        Assertions.assertEquals(client.getId(), dto.getClientId());
        Assertions.assertEquals(dto.getTransactionDate().toInstant(), now.toInstant());
    }

    private void createTransactions(Client client, Date now) {
        Date yesterday = Date.from(Instant.now().minus(1, ChronoUnit.DAYS));
        Date tomorrow = Date.from(Instant.now().plus(1, ChronoUnit.DAYS));
        createTransaction(now, client.getId(), HUNDRED, true);
        createTransaction(now, client.getId(), TWO_HUNDRED, false);
        createTransaction(yesterday, client.getId(), HUNDRED, true);
        createTransaction(yesterday, client.getId(), HUNDRED, false);
        createTransaction(tomorrow, client.getId(), HUNDRED, true);
        createTransaction(tomorrow, client.getId(), HUNDRED, false);
    }

    private void createTransaction(Date date, Long clientId, BigDecimal value, boolean withdraw){
        Transaction transaction = new Transaction();
        transaction.setTransactionDate(date);
        transaction.setAmount(value);
        transaction.setClientId(clientId);
        transaction.setWithdraw(withdraw);
        transactionRepository.save(transaction);
    }


}
