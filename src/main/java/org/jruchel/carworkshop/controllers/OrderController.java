package org.jruchel.carworkshop.controllers;

import org.jruchel.carworkshop.entities.Client;
import org.jruchel.carworkshop.entities.Order;
import org.jruchel.carworkshop.services.ClientService;
import org.jruchel.carworkshop.services.OrderService;
import org.jruchel.carworkshop.validation.ValidationErrorPasser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;


@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;
    private final ClientService clientService;
    private ValidationErrorPasser errorPasser;

    public OrderController(OrderService orderService, ClientService clientService) {
        this.orderService = orderService;
        this.errorPasser = ValidationErrorPasser.getInstance();
        this.clientService = clientService;
    }

    @GetMapping("/unresponded")
    public ResponseEntity<List<Order>> getUnrespondedOrders() {
        return new ResponseEntity<>(orderService.getUnrespondedOrders(), HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<String> addOrder(@RequestBody Order order) {
        Client client = order.getClient();
        if (client == null) return new ResponseEntity<>("Client cannot be empty.", HttpStatus.CONFLICT);
        if (client.getEmail() == null)
            return new ResponseEntity<>("Client's email cannot be empty.", HttpStatus.CONFLICT);
        if (client.getPhoneNumber() == null) client.setPhoneNumber("");
        if ((client.getEmail().isEmpty() && client.getPhoneNumber().isEmpty()))
            return new ResponseEntity<>("Client must not be empty.", HttpStatus.CONFLICT);
        try {
            Client clientFromDB = clientService.findByEmail(client.getEmail());
            if (clientFromDB == null) {
                client.addOrder(order);
            } else {
                clientFromDB.addOrder(order);
                if (client.getPhoneNumber() != null && !client.getPhoneNumber().isEmpty()) {
                    clientFromDB.setPhoneNumber(client.getPhoneNumber());
                }
                order.setClient(clientFromDB);
            }
            order.setDate(new Date());
            orderService.save(order);
        } catch (Exception ex) {
            String message = errorPasser.getMessagesAsString();
            if (message.isEmpty()) message = ex.getMessage();
            return new ResponseEntity<>(message, HttpStatus.NOT_ACCEPTABLE);
        }
        return new ResponseEntity<>("Order has been submited successfully", HttpStatus.OK);
    }

}
