package com.test.currency.service;

import com.test.currency.domain.Client;
import com.test.currency.domain.dto.ClientDTO;
import com.test.currency.mapper.ClientMapper;
import com.test.currency.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClientService {
    @Autowired
    ClientRepository clientRepository;

    @Autowired
    ClientMapper clientMapper;

    public ClientDTO findCliendById(Long id){
        Optional<Client> optClient = clientRepository.findById(id);
        if(optClient.isPresent()){
            return clientMapper.toDto(optClient.get());
        }else{
            throw new RuntimeException("Client not found");
        }
    }

    public ClientDTO findClientByAccount(String accountNumber){
        Optional<Client> optClient = clientRepository.findByAccountNumber(accountNumber);
        if(optClient.isPresent()){
            return clientMapper.toDto(optClient.get());
        }else{
            throw new RuntimeException("Client not found");
        }
    }

    public List<ClientDTO> findAllClients(){
        return clientRepository.findAll().stream().map(clientMapper::toDto).toList();
    }

    public void saveClient(ClientDTO clientDTO) {
        try {
            clientRepository.save(clientMapper.toEntity(clientDTO));
        }catch(DataIntegrityViolationException e){
            throw new RuntimeException("Acccount number already in use");
        }
    }
}
