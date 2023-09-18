package com.dsin.teste.Controller;

import com.dsin.teste.model.Client;
import com.dsin.teste.service.Impl.ClientServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/client/config")
public class ClientController {
    private final ClientServiceImpl clientService;

    @Autowired
    public ClientController(ClientServiceImpl clientService) {
        this.clientService = clientService;
    }

    @GetMapping()
    public ResponseEntity<List<Client>> getClients() {
        List<Client> clients = clientService.getAllClients();
        return ResponseEntity.ok(clients);
    }
    @GetMapping(path = "/{clientId}")
    public ResponseEntity<Client> findClientById(@PathVariable Long clientId) {
        Client client = clientService.getClientById(clientId)
                .orElseThrow(() -> new EntityNotFoundException("Cliente não encontrado " + clientId));
        return ResponseEntity.status(HttpStatus.OK).body(client);
    }


    @PostMapping(path = "/register")
    public ResponseEntity<String> registerClient(@RequestBody Client client) {
        clientService.create(client);
        return ResponseEntity.status(HttpStatus.OK).body("Cliente cadastrado!");
    }


    @DeleteMapping(path = "/{clientId}")
    public ResponseEntity<String> deleteClient(@PathVariable Long clientId) {
        clientService.deleteClientById(clientId);
        return ResponseEntity.status(HttpStatus.OK).body("O client com o id" + clientId + "foi deletado com sucesso!");
    }

    @PutMapping(path = "/{clientId}")
    public ResponseEntity<Client> clientUpdate(
            @PathVariable("clientId") Long clientId,
            @RequestBody Client updatedClient) {
        Client updated = clientService.update(clientId, updatedClient);
        return ResponseEntity.ok(updated);
    }

}


