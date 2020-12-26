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

import javax.websocket.server.PathParam;
import java.util.List;

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
    public ResponseEntity<List<Client>> getUnrespondedClients(@RequestParam(required = false, defaultValue = "1", value = "page") int page, @RequestParam(required = false, defaultValue = "10", value = "elements") int elements) {
        if (page < 1) page = 1;
        if (elements < 1) elements = 1;
        return new ResponseEntity<>(clientService.getUnrespondedClients(page, elements), HttpStatus.OK);
    }


    @GetMapping("/unresponded/orders")
    public ResponseEntity<List<Order>> getUnrespondedOrders(@RequestParam(required = false, defaultValue = "1", value = "page") int page, @RequestParam(required = false, defaultValue = "10", value = "elements") int elements) {
        if (page < 1) page = 1;
        if (elements < 1) elements = 1;
        return new ResponseEntity<>(orderService.getUnrespondedOrders(page, elements), HttpStatus.OK);
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
    public ResponseEntity<Client> getUnrespondedOrders(@RequestParam(value = "id") Integer id, @PathParam(value = "email") String email) {
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
