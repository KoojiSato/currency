package com.test.currency.rest;

import com.test.currency.domain.dto.ClientDTO;
import com.test.currency.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/client")
public class ClientResource {

    @Autowired
    ClientService clientService;

    @PutMapping("/newClient")
    public ResponseEntity<Void> addNewClient(@RequestBody ClientDTO clientDTO){
        try {
            clientService.saveClient(clientDTO);
            return new ResponseEntity<>(HttpStatus.OK);
        }catch (RuntimeException e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/allClients")
    public ResponseEntity<List<ClientDTO>> findAllClients(){
        return new ResponseEntity<>(clientService.findAllClients(), HttpStatus.OK);
    }

}
