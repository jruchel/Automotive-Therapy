package org.jruchel.carworkshop.services;

import org.jruchel.carworkshop.entities.Order;
import org.jruchel.carworkshop.repositories.OrderRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public Order findById(Integer id) {
        Optional<Order> optional = orderRepository.findById(id);
        return optional.orElse(null);
    }

    public List<Order> getUnrespondedOrders(int page, int elements) {
        return orderRepository.getUnrespondedOrders(PageRequest.of(page - 1, elements));
    }

    public void save(Order order) {
        orderRepository.save(order);
    }
}