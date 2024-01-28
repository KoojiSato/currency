package com.test.currency;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.test.currency.domain.Client;
import com.test.currency.repository.ClientRepository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


public class TestUtils {

    public static final String EXCLUSIVE_ACCOUNT = "123458";
    public static final String NO_FUND_ACCOUNT = "123457";
    public static final String NORMAL_ACCOUNT = "123455";
    private static int balanceTenThousand = 10000;

    ClientRepository clientRepository;



    public TestUtils(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public void createClients(){
        clientRepository.deleteAll();
        clientRepository.save(clientExclusive());
        clientRepository.save(clientNoFund());
        clientRepository.save(normalClient());
        clientRepository.flush();
    }

    public void clearClients(){
        List<Client> clients = clientRepository.findAll();
        clientRepository.deleteAll(clients);
        clientRepository.flush();
    }

    public Client findFirst(){
        return clientRepository.findAll().stream().findFirst().get();
    }

    private Client normalClient() {
        Client normalClient = new Client();
        normalClient.setExclusivePlan(false);
        normalClient.setBalance(BigDecimal.valueOf(balanceTenThousand));
        normalClient.setName("normalClient");
        normalClient.setAccountNumber(NORMAL_ACCOUNT);
        normalClient.setBirthDate(new Date());
        return normalClient;
    }

    private Client clientExclusive(){
        Client clientExclusive = new Client();
        clientExclusive.setExclusivePlan(true);
        clientExclusive.setBalance(BigDecimal.valueOf(balanceTenThousand));
        clientExclusive.setName("exclusiveClient");
        clientExclusive.setAccountNumber(EXCLUSIVE_ACCOUNT);
        clientExclusive.setBirthDate(new Date());
        return clientExclusive;
    }

    private Client clientNoFund(){
        Client NoFundClient = new Client();
        NoFundClient.setExclusivePlan(false);
        NoFundClient.setBalance(BigDecimal.ZERO);
        NoFundClient.setName("noFundClient");
        NoFundClient.setAccountNumber(NO_FUND_ACCOUNT);
        NoFundClient.setBirthDate(new Date());
        return NoFundClient;
    }

    public String getNoFundAccount(){
        return NO_FUND_ACCOUNT;
    }

    public static String toJson(Object object) throws JsonProcessingException {
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        return ow.writeValueAsString(object);
    }

}
