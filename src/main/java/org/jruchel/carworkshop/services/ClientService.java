package org.jruchel.carworkshop.services;

import org.jruchel.carworkshop.entities.Client;
import org.jruchel.carworkshop.entities.Order;
import org.jruchel.carworkshop.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ClientService {

    private final ClientRepository clientRepository;
    private final OrderService orderService;


    @Autowired
    public ClientService(ClientRepository clientRepository, OrderService orderService) {
        this.clientRepository = clientRepository;
        this.orderService = orderService;
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

    public List<Client> getUnrespondedClientsSorted(int page, int elements) {
        try {
            clientRepository.createClientsByLatestOrderUnresponded();
        } catch (Exception ignored) {

        }
        List<Integer> resultIds = clientRepository.getClientsByLatestOrder(PageRequest.of(page - 1, elements));

        List<Client> result = new ArrayList<>();
        resultIds.forEach(i -> result.add(clientRepository.findById(i).orElse(null)));
        try {
            clientRepository.dropClientsByLatestOrder();
        } catch (Exception ignored) {

        }
        return result;
    }

    public List<Client> getAwaitingClients(int page, int elements) {
        return clientRepository.getAwaitingClients(PageRequest.of(page - 1, elements));
    }

    public void save(Client client) {
        clientRepository.save(client);
    }


}