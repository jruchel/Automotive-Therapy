package org.jruchel.carworkshop.services;

import org.jruchel.carworkshop.entities.Client;
import org.jruchel.carworkshop.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ClientService {

    private ClientRepository clientRepository;
    private final OrderService orderService;

    public ClientService(OrderService orderService) {
        this.orderService = orderService;
    }

    @Autowired
    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }


    public Client findById(Integer id) {
        Optional<Client> optional = clientRepository.findById(id);
        return optional.orElse(null);
    }

    public Client findByPhone(String phone) {
        return clientRepository.findByPhone(phone);
    }

    public Client findByEmail(String email) {
        return clientRepository.findByEmail(email);
    }

    public List<Client> getUnrespondedClients(int page, int elements) {
        return clientRepository.getUnrespondedClients(PageRequest.of(page - 1, elements));
    }

    public void save(Client client) {
        clientRepository.save(client);
    }
}