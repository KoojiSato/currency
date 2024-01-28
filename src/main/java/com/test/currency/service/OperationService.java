package com.test.currency.service;

import com.test.currency.domain.Transaction;
import com.test.currency.domain.dto.ClientDTO;
import com.test.currency.domain.dto.TransactionDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;

@Service
public class OperationService {

    private static BigDecimal HUNDRED = BigDecimal.valueOf(100);
    private static BigDecimal THREE_HUNDRED = BigDecimal.valueOf(300);

    private static BigDecimal minTax = BigDecimal.valueOf(0.004);

    private static BigDecimal maxTax = BigDecimal.valueOf(0.01);

    @Autowired
    ClientService clientService;

    @Autowired
    TransactionService transactionService;

    public void withdrawOperation(BigDecimal value, Long id){
        ClientDTO clientDTO = clientService.findCliendById(id);
        withdrawFromClient(value, clientDTO);
    }

    public void withdrawOperation(BigDecimal value, String accountNumber){
        ClientDTO clientDTO = clientService.findClientByAccount(accountNumber);
        withdrawFromClient(value, clientDTO);
    }

    public void deposityOperation(BigDecimal value, String accountNumber){
        ClientDTO clientDTO = clientService.findClientByAccount(accountNumber);
        deposityToClient(clientDTO, value);
    }

    public void deposityOperation(BigDecimal value, Long id){
        ClientDTO clientDTO = clientService.findCliendById(id);
        deposityToClient(clientDTO, value);
    }

    public List<TransactionDTO> findTransactionsDateClient(String accountNumber, Date transactionDate){
        ClientDTO clientDTO = clientService.findClientByAccount(accountNumber);
        return transactionService.findByDateClient(transactionDate, clientDTO.getId());
    }

    private void withdrawFromClient(BigDecimal value, ClientDTO clientDTO) {
        if(value.compareTo(HUNDRED) > 0 && !clientDTO.isExclusivePlan()){
            clientService.saveClient(taxWithdraw(clientDTO, value));
        }else{
            clientService.saveClient(withdraw(clientDTO, value, BigDecimal.ZERO));
        }
    }

    private ClientDTO taxWithdraw(ClientDTO clientDTO, BigDecimal value){
        if(value.compareTo(THREE_HUNDRED) > 0){
            withdraw(clientDTO, value, maxTax);
        }else{
            withdraw(clientDTO, value, minTax);
        }
        return clientDTO;
    }

    private ClientDTO withdraw(ClientDTO clientDTO, BigDecimal value, BigDecimal tax){
        BigDecimal withdrawTotal = value.add(value.multiply(tax)).setScale(2, RoundingMode.UP);
        BigDecimal balance = clientDTO.getBalance();
        if(balance.compareTo(withdrawTotal) < 0){
            throw new RuntimeException("not enough funds");
        }
        balance = balance.subtract(withdrawTotal);
        clientDTO.setBalance(balance);
        transactionService.createNewTransaction(withdrawTotal, clientDTO.getId(), true);
        return clientDTO;
    }

    private void deposityToClient(ClientDTO clientDTO, BigDecimal value){
        clientService.saveClient(deposity(clientDTO, value));
    }

    private ClientDTO deposity(ClientDTO clientDTO, BigDecimal value){
        clientDTO.setBalance(clientDTO.getBalance().add(value));
        transactionService.createNewTransaction(value, clientDTO.getId(), false);
        return clientDTO;
    }







}
