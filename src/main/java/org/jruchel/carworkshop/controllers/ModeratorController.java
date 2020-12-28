package org.jruchel.carworkshop.controllers;

import org.jruchel.carworkshop.entities.Client;
import org.jruchel.carworkshop.entities.Email;
import org.jruchel.carworkshop.entities.Order;
import org.jruchel.carworkshop.services.ClientService;
import org.jruchel.carworkshop.services.MailingService;
import org.jruchel.carworkshop.services.OrderService;
import org.jruchel.carworkshop.utils.Properties;
import org.jruchel.carworkshop.validation.ValidationErrorPasser;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/moderator")
public class ModeratorController {

    private final ClientService clientService;
    private final OrderService orderService;
    private final MailingService mailingService;
    private final ValidationErrorPasser errorPasser;

    public ModeratorController(ClientService clientService, OrderService orderService, MailingService mailingService) {
        this.clientService = clientService;
        this.orderService = orderService;
        this.mailingService = mailingService;
        this.errorPasser = ValidationErrorPasser.getInstance();
    }

    @PostMapping("/add")
    public ResponseEntity<String> addClient(@RequestBody Client client) {
        for (Order order : client.getOrders()) {
            order.setClient(client);
        }
        if (clientService.findByPhone(client.getPhoneNumber()) != null || clientService.findByEmail(client.getEmail()) != null)
            return new ResponseEntity<>("This client already exists in the database", HttpStatus.CONFLICT);
        try {
            clientService.save(client);
        } catch (Exception ex) {
            return new ResponseEntity<>(errorPasser.getMessagesAsString(), HttpStatus.NOT_ACCEPTABLE);
        }
        if (clientService.findByPhone(client.getPhoneNumber()) != null || clientService.findByEmail(client.getEmail()) != null)
            return new ResponseEntity<>("Client added successfully to the database", HttpStatus.OK);
        return new ResponseEntity<>("Failed to add client to the database", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping("/clients/unresponded")
    public ResponseEntity<List<Client>> getUnrespondedClients(@RequestParam(required = false, defaultValue = "1", value = "page") int page, @RequestParam(required = false, defaultValue = "10", value = "elements") int elements) {
        if (page < 1) page = 1;
        if (elements < 1) elements = 1;
        return new ResponseEntity<>(clientService.getUnrespondedClients(page, elements), HttpStatus.OK);
    }

    @GetMapping("/orders/unresponded")
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

    @PostMapping("/orders/respond")
    public ResponseEntity<Order> respondToOrder(@RequestBody int orderID) {
        Order order = orderService.findById(orderID);
        order.setResponed(true);
        orderService.save(order);
        return new ResponseEntity<>(order, HttpStatus.OK);
    }

    @GetMapping("/clients/awaiting")
    public ResponseEntity<List<Client>> getAwaitingClients(@RequestParam(required = false, defaultValue = "1", name = "page") int page, @RequestParam(required = false, defaultValue = "1", name = "elements") int elements) {
        return new ResponseEntity<>(clientService.getAwaitingClients(page, elements), HttpStatus.OK);
    }

    @PostMapping("/orders/complete")
    public ResponseEntity<Order> completeOrder(@RequestBody int orderID) {
        try {
            Order order = orderService.findById(orderID);
            order.setComplete(true);
            order.setResponed(true);
            orderService.save(order);
            Email mail = new Email();
            mail.setTo(order.getClient().getEmail());
            try {
                mail.setSubject(Properties.getInstance().readProperty("mailing.complete.subject"));
                Properties properties = Properties.getInstance();
                String address = properties.readProperty("workshop.address");
                String workingHours = properties.readProperty("workshop.working-hours");
                String message = String.format(properties.readProperty("mailing.complete.content"), address, workingHours);
                mail.setMessage(message);
            } catch (IOException e) {
                mail.setSubject("Powiadomienie o zakonczeniu.");
                mail.setMessage("Samochod jest gotowy do odbioru.");
            }

            mailingService.sendEmail(mail, true);
            return new ResponseEntity<>(order, HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
