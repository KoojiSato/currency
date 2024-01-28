package com.test.currency.service;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.test.currency.CurrencyApplication;
import com.test.currency.TestUtils;
import com.test.currency.domain.Client;
import com.test.currency.domain.dto.ClientDTO;
import com.test.currency.repository.ClientRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Date;
import java.util.List;

@SpringBootTest(classes = CurrencyApplication.class)
public class ClientServiceIT {

    @Autowired
    ClientRepository clientRepository;

    TestUtils testUtils;

    @Autowired
    ClientService clientService;

    @BeforeEach
    void createClients(){
        testUtils = new TestUtils(clientRepository);
        testUtils.createClients();
    }

    @AfterEach
    void clearClients(){
        testUtils.clearClients();
    }

    @Test
    public void testFindByAccountNumber(){
        ClientDTO client = clientService.findClientByAccount("123455");
        Assertions.assertEquals("normalClient", client.getName());
    }

    @Test
    public void testFindById(){
        List<ClientDTO> clients = clientService.findAllClients();
        Assertions.assertEquals(clients.size(), 3);
        for(ClientDTO client : clients) {
            ClientDTO dto = clientService.findCliendById(client.getId());
            Assertions.assertEquals(dto.getName(), client.getName());
            Assertions.assertEquals(dto.getBalance(), client.getBalance());
            Assertions.assertEquals(dto.isExclusivePlan(), client.isExclusivePlan());
            Assertions.assertEquals(dto.getId(), client.getId());
            Assertions.assertEquals(dto.getAccountNumber(), client.getAccountNumber());
        }
    }

    @Test
    public void testSaveClient(){
        String account = "020202";
        Date now = Date.from(Instant.now());
        ClientDTO clientDTO = new ClientDTO();
        clientDTO.setBalance(BigDecimal.valueOf(66));
        clientDTO.setName("newClient");
        clientDTO.setAccountNumber(account);
        clientDTO.setBirthDate(now);
        clientDTO.setExclusivePlan(true);
        clientService.saveClient(clientDTO);

        List<ClientDTO> allClients = clientService.findAllClients();
        Assertions.assertEquals(4, allClients.size());
        ClientDTO newClient = clientService.findClientByAccount(account);
        Assertions.assertEquals(clientDTO.getAccountNumber(), newClient.getAccountNumber());
        Assertions.assertEquals(clientDTO.getName(), newClient.getName());
        Assertions.assertEquals(clientDTO.getBalance().setScale(2), newClient.getBalance().setScale(2));
        Assertions.assertEquals(now, newClient.getBirthDate());
    }


}
