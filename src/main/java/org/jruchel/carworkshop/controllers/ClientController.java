package org.jruchel.carworkshop.controllers;

import org.jruchel.carworkshop.entities.Client;
import org.jruchel.carworkshop.entities.Order;
import org.jruchel.carworkshop.services.ClientService;
import org.jruchel.carworkshop.services.MailingService;
import org.jruchel.carworkshop.validation.ValidationErrorPasser;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/clients")
public class ClientController {

    private final ClientService clientService;
    private final ValidationErrorPasser errorPasser;
    private final MailingService mailingService;

    public ClientController(ClientService clientService, MailingService mailingService) {
        this.clientService = clientService;
        this.errorPasser = ValidationErrorPasser.getInstance();
        this.mailingService = mailingService;
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

}
