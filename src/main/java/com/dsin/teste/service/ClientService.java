package com.dsin.teste.service;

import com.dsin.teste.model.Client;

import java.util.List;
import java.util.Optional;


public interface ClientService {

    Client create(Client client);
    List<Client> getAllClients();
    Optional<Client> getClientById(Long id);
    Client update(Long clientId, Client updatedClient);
    void deleteClientById(Long id);




}
