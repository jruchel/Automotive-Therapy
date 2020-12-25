package org.jruchel.carworkshop.controllers;

import org.jruchel.carworkshop.entities.Client;
import org.jruchel.carworkshop.entities.Email;
import org.jruchel.carworkshop.entities.Order;
import org.jruchel.carworkshop.services.ClientService;
import org.jruchel.carworkshop.services.MailingService;
import org.jruchel.carworkshop.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.websocket.server.PathParam;
import java.io.IOException;
import java.util.List;
import java.util.Set;
@CrossOrigin
@RestController
@RequestMapping("/moderator")
public class ModeratorController {

    @Autowired
    private ClientService clientService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private MailingService mailingService;

    @GetMapping("/unresponded/clients")
    public ResponseEntity<Set<Client>> getUnrespondedClients() {
        return new ResponseEntity<>(clientService.getUnrespondedClients(), HttpStatus.OK);
    }


    @GetMapping("/unresponded/orders")
    public ResponseEntity<List<Order>> getUnrespondedOrders() {
        return new ResponseEntity<>(orderService.getUnrespondedOrders(), HttpStatus.OK);
    }

    @PostMapping("/mail")
    public ResponseEntity<String> sendEmail(@RequestBody Email email) {
        try {
            email.setFrom(org.jruchel.carworkshop.utils.Properties.getInstance().readProperty("mail.sender"));
            mailingService.sendEmail(email, false);
            return new ResponseEntity<>("Message sent.", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        }
    }

    @GetMapping("/client")
    public ResponseEntity<Client> getUnrespondedOrders(@PathParam(value = "id") Integer id, @PathParam(value = "email") String email) {
        Client client = null;
        if (id != null) client = clientService.findById(id);
        if (client == null) client = clientService.findByEmail(email);
        return new ResponseEntity<>(client, HttpStatus.OK);
    }

    @PostMapping("/order/respond")
    public ResponseEntity<Order> respondeToOrder(@RequestBody int orderID) {
        Order order = orderService.findById(orderID);
        order.setResponed(true);
        orderService.save(order);
        return new ResponseEntity<>(order, HttpStatus.OK);
    }

}
