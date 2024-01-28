package com.test.currency.rest;

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
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Optional;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = CurrencyApplication.class)
@AutoConfigureMockMvc
public class ClientResourceIT {

    TestUtils testUtils;

    @Autowired
    MockMvc mvc;

    @Autowired
    ClientRepository clientRepository;

    @BeforeEach
    void createClients(){
        testUtils = new TestUtils(clientRepository);
        testUtils.createClients();
    }

    @AfterEach
    void clearTest(){
        testUtils.clearClients();
    }

    @Test
    public void testCreateNewClient() throws Exception{
        ClientDTO clientDTO = createClientDTO();
        String json = TestUtils.toJson(clientDTO);
        mvc.perform(MockMvcRequestBuilders.put("/client/newClient").content(json).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
        Optional<Client> optClient = clientRepository.findByAccountNumber("01");
        Assertions.assertEquals(optClient.isPresent(), true);
        Client client = optClient.get();
        Assertions.assertEquals(client.getBalance(), BigDecimal.ZERO.setScale(2));
        Assertions.assertEquals(client.getName(), "NewClient");
        Assertions.assertEquals(client.isExclusivePlan(), false);
    }

    @Test
    public void testCreateNewClientSameAccount() throws Exception{
        ClientDTO clientDTO = createClientDTO();
        clientDTO.setAccountNumber(TestUtils.NORMAL_ACCOUNT);
        String json = TestUtils.toJson(clientDTO);
        mvc.perform(MockMvcRequestBuilders.put("/client/newClient").content(json).contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().is5xxServerError());
    }

    @Test
    public void testGetAllClients() throws Exception{
        mvc.perform(MockMvcRequestBuilders.get("/client/allClients")).andExpect(status().isOk())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.[0].name").value("exclusiveClient"))
                .andExpect(jsonPath("$.[0].accountNumber").value(TestUtils.EXCLUSIVE_ACCOUNT))
                .andExpect(jsonPath("$.[0].exclusivePlan").value(true))

                .andExpect(jsonPath("$.[1].name").value("noFundClient"))
                .andExpect(jsonPath("$.[1].accountNumber").value(TestUtils.NO_FUND_ACCOUNT))
                .andExpect(jsonPath("$.[1].exclusivePlan").value(false))

                .andExpect(jsonPath("$.[2].name").value("normalClient"))
                .andExpect(jsonPath("$.[2].accountNumber").value(TestUtils.NORMAL_ACCOUNT))
                .andExpect(jsonPath("$.[2].exclusivePlan").value(false));
    }

    private static ClientDTO createClientDTO() {
        ClientDTO clientDTO = new ClientDTO();
        clientDTO.setBalance(BigDecimal.ZERO);
        clientDTO.setBirthDate(new Date());
        clientDTO.setName("NewClient");
        clientDTO.setAccountNumber("01");
        clientDTO.setExclusivePlan(false);
        return clientDTO;
    }

}
