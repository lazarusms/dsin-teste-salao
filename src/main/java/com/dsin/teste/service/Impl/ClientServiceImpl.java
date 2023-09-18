package com.dsin.teste.service.Impl;

import com.dsin.teste.Controller.AppointmentClientController;
import com.dsin.teste.model.Client;
import com.dsin.teste.repository.ClientRepository;
import com.dsin.teste.service.ClientService;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class ClientServiceImpl implements ClientService {
    private final ClientRepository clientRepository;
    private final Logger logger = LoggerFactory.getLogger(AppointmentClientController.class);


    public ClientServiceImpl(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Override
    public Client create(Client client) {
        return clientRepository.save(client);
    }

    @Override
    public List<Client> getAllClients() {
        return clientRepository.findAll();
    }

    @Override
    public Optional<Client> getClientById(Long id) {
        return clientRepository.findById(id);
    }

    @Override
    @Transactional
    public Client update(Long clientId, Client updatedClient) {
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new IllegalStateException("O agendamento com o id " + clientId + " não existe"));
        updateName(client, updatedClient.getFirstName());
        updateEmail(client, updatedClient.getEmail());
        updatePhoneNumber(client, updatedClient.getPhoneNumber());
        logger.info("Update realizado");
        return client;
    }

    @Override
    public void deleteClientById(Long id) {
        boolean clientExists = clientRepository.existsById(id);
        if (!clientExists){
            throw new IllegalStateException("O cliente com id " + id + " não existe");
        }
        clientRepository.deleteById(id);

    }
    private void updateName(Client client, String name) {
        if(name != null && name.length() > 0 && !Objects.equals(client.getFirstName(), name)) {
            client.setFirstName(name);
        }
    }
    private void updatePhoneNumber(Client client, String phoneNumber) {
        if(phoneNumber != null && phoneNumber.length() > 0 && !Objects.equals(client.getPhoneNumber(), phoneNumber)) {
            Optional<Client> clientOptional = clientRepository.findClientByPhone(phoneNumber);
            if(clientOptional.isPresent()){
                throw new IllegalStateException("esse número de telefone já está sendo usado");
            }
            client.setEmail(phoneNumber);
        }
    }
    private void updateEmail(Client client, String email) {
        if(email != null && email.length() > 0 && !Objects.equals(client.getEmail(), email)) {
            Optional<Client> clientOptional = clientRepository.findClientByEmail(email);
            if(clientOptional.isPresent()){
                throw new IllegalStateException("email já está sendo usado");
            }
            client.setEmail(email);
        }
    }


}
