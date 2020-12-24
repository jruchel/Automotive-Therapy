package org.jruchel.carworkshop.services;

import org.jruchel.carworkshop.entities.Client;
import org.jruchel.carworkshop.repositories.ClientRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ClientService {

    private final ClientRepository clientRepository;

    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public Client findById(Integer id) {
        Optional<Client> optional = clientRepository.findById(id);
        return optional.orElse(null);
    }

    public void save(Client client) {
        clientRepository.save(client);
    }
}