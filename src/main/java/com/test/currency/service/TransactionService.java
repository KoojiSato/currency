package com.test.currency.service;

import com.test.currency.domain.Transaction;
import com.test.currency.domain.dto.TransactionDTO;
import com.test.currency.mapper.TransactionMapper;
import com.test.currency.repository.TransactionRepository;
import com.test.currency.util.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Service
public class TransactionService {

    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    TransactionMapper mapper;

    public List<TransactionDTO> findByDateClient(Date transactionDate, Long clientId){
        Date startDay = DateUtils.atStartOfDay(transactionDate);
        Date endDay = DateUtils.atEndOfDay(transactionDate);
        return transactionRepository.findByTransactionDateClient(startDay, endDay, clientId).stream().map(mapper::toDto).toList();
    }

    public List<TransactionDTO> findByDate(Date transactionDate){
        Date startDay = DateUtils.atStartOfDay(transactionDate);
        Date endDay = DateUtils.atEndOfDay(transactionDate);
        return transactionRepository.findByTransactionDate(startDay, endDay).stream().map(mapper::toDto).toList();
    }

    public void createNewTransaction(BigDecimal value, Long clientId, boolean withdraw){
        Transaction transaction = new Transaction();
        transaction.setTransactionDate(new Date());
        transaction.setAmount(value.setScale(2));
        transaction.setClientId(clientId);
        transaction.setWithdraw(withdraw);
        transactionRepository.save(transaction);
    }

    public List<TransactionDTO> findAll(){
        return transactionRepository.findAll().stream().map(mapper::toDto).toList();
    }


}
