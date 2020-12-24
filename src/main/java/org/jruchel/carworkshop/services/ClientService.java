package org.jruchel.carworkshop.services;

import org.aspectj.weaver.ast.Or;
import org.jruchel.carworkshop.entities.Client;
import org.jruchel.carworkshop.entities.Order;
import org.jruchel.carworkshop.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ClientService {

    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private OrderService orderService;


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

    public Set<Client> getUnrespondedClients() {
        List<Order> unrespondedOrders = orderService.getUnrespondedOrders();
        Set<Client> unrespondedClients = new HashSet<>();
        for (Order o : unrespondedOrders) {
            unrespondedClients.add(o.getClient());
        }
        return unrespondedClients;
    }

    public void save(Client client) {
        clientRepository.save(client);
    }
}