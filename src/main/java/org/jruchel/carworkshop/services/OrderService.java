package org.jruchel.carworkshop.services;

import org.jruchel.carworkshop.entities.Order;
import org.jruchel.carworkshop.repositories.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    public Order findById(Integer id) {
        Optional<Order> optional = orderRepository.findById(id);
        return optional.orElse(null);
    }

    public void save(Order order) {
        orderRepository.save(order);
    }
}