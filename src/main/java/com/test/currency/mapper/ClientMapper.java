package com.test.currency.mapper;

import com.test.currency.domain.Client;
import com.test.currency.domain.dto.ClientDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ClientMapper {

    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "exclusivePlan", target = "exclusivePlan")
    @Mapping(source = "balance", target = "balance")
    @Mapping(source = "accountNumber", target = "accountNumber")
    @Mapping(source = "birthDate", target = "birthDate")
    ClientDTO toDto(Client client);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "exclusivePlan", target = "exclusivePlan")
    @Mapping(source = "balance", target = "balance")
    @Mapping(source = "accountNumber", target = "accountNumber")
    @Mapping(source = "birthDate", target = "birthDate")
    Client toEntity(ClientDTO clientDTO);
}
