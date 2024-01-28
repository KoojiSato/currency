package com.test.currency.rest;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.test.currency.domain.Transaction;
import com.test.currency.domain.dto.OperationDTO;
import com.test.currency.domain.dto.TransactionDTO;
import com.test.currency.service.OperationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api")
public class OperationResource {

    @Autowired
    OperationService operationService;

    @PostMapping("/withdraw")
    public ResponseEntity<Void> withdrawValue(@RequestBody OperationDTO operationDTO){
        try{
            operationService.withdrawOperation(operationDTO.getValue(), operationDTO.getAccountNumber());
        }catch (RuntimeException e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return  new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/deposity")
    public void deposityValue(@RequestBody OperationDTO operationDTO){
        operationService.deposityOperation(operationDTO.getValue(), operationDTO.getAccountNumber());
    }

    @GetMapping("/operations")
    public ResponseEntity<List<TransactionDTO>> getOperationsDate(@RequestParam @DateTimeFormat(pattern="dd/MM/yyyy") Date transactionDate, @RequestParam String accountNumber){
        List<TransactionDTO> transactions = operationService.findTransactionsDateClient(accountNumber, transactionDate);
        return new ResponseEntity<>(transactions, HttpStatus.OK);
    }

}
