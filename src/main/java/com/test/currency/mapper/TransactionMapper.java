package com.test.currency.mapper;

import com.test.currency.domain.Transaction;
import com.test.currency.domain.dto.TransactionDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TransactionMapper {

    @Mapping(source = "id", target = "id")
    @Mapping(source = "transactionDate", target = "transactionDate")
    @Mapping(source = "amount", target = "amount")
    @Mapping(source = "withdraw", target = "withdraw")
    @Mapping(source = "clientId", target = "clientId")
    TransactionDTO toDto(Transaction transaction);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "transactionDate", target = "transactionDate")
    @Mapping(source = "amount", target = "amount")
    @Mapping(source = "withdraw", target = "withdraw")
    @Mapping(source = "clientId", target = "clientId")
    Transaction toEntity(TransactionDTO transactionDTO);

}
