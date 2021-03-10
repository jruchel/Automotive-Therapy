package org.jruchel.carworkshop.controllers;

import org.jruchel.carworkshop.automation.Controller;
import org.jruchel.carworkshop.automation.SecuredMapping;
import org.jruchel.carworkshop.entities.Client;
import org.jruchel.carworkshop.entities.ClientOrderPair;
import org.jruchel.carworkshop.entities.Order;
import org.jruchel.carworkshop.services.ClientService;
import org.jruchel.carworkshop.services.MailingService;
import org.jruchel.carworkshop.services.OrderService;
import org.jruchel.carworkshop.configuration.Properties;
import org.jruchel.carworkshop.validation.ValidationErrorPasser;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@CrossOrigin
@RestController
@RequestMapping("/orders")
public class OrderController extends Controller {

    private final OrderService orderService;
    private final ClientService clientService;
    private final ValidationErrorPasser errorPasser;
    private final MailingService mailingService;
    private final Properties properties;

    public OrderController(Properties properties, OrderService orderService, ClientService clientService, MailingService mailingService) {
        this.properties = properties;
        this.orderService = orderService;
        this.errorPasser = ValidationErrorPasser.getInstance();
        this.clientService = clientService;
        this.mailingService = mailingService;
    }

    @SecuredMapping(path = "/add", method = RequestMethod.POST)
    public ResponseEntity<String> addOrder(@RequestBody ClientOrderPair clientOrderPair) {
        return addOrder(clientOrderPair, false);
    }

    @SecuredMapping(path = "/add/moderator", method = RequestMethod.POST, role = "moderator")
    public ResponseEntity<String> addOrderAsModerator(@RequestBody ClientOrderPair clientOrderPair) {
        return addOrder(clientOrderPair, true);
    }

    private Order getCleanOrder(Order order) {
        Order cleanOrder = new Order();
        cleanOrder.setClient(order.getClient());
        cleanOrder.setDescription(order.getDescription());
        cleanOrder.setStatus(Order.Status.pending);
        return cleanOrder;
    }

    private ResponseEntity<String> addOrder(ClientOrderPair clientOrderPair, boolean moderator) {
        Client client = clientOrderPair.getClient();
        Order order = getCleanOrder(clientOrderPair.getOrder());
        if (client == null)
            return new ResponseEntity<>("Dane kontaktowe muszą zostać wypełnione.", HttpStatus.CONFLICT);
        if (client.getPhoneNumber() == null) client.setPhoneNumber("");
        try {
            //Checking if the client already exists in the database
            Client clientFromDB = clientService.findByEmail(client.getEmail());
            //If not using the client object to add the new client to the database
            if (clientFromDB == null) {
                client.addOrder(order);
            } else {
                if (clientFromDB.getOrders().stream().anyMatch(o -> o.getDescription().equals(order.getDescription()) && !o.getStatus().equals(Order.Status.completed))) {
                    return new ResponseEntity<>("Na twoim koncie znajduje się już identyczne zlecenie.", HttpStatus.CONFLICT);
                }
                clientFromDB.addOrder(order);
                //Only updating the phone number if it changes to another valid phone number
                if (client.getPhoneNumber() != null && !client.getPhoneNumber().isEmpty() && client.getPhoneNumber().matches(properties.getPhonePattern())) {
                    clientFromDB.setPhoneNumber(client.getPhoneNumber());
                }
                //Making sure an order like this isn't already in the database
                order.setClient(clientFromDB);
            }
            order.setDate(new Date());
            //If the client is null it means they weren't found in the database, adding new client
            if (order.getClient() == null) order.setClient(client);
            order.getClient().setLastOrder(new Date());
            orderService.save(order);
            if (!moderator)
                mailingService.sendEmail(order.getClient().getEmail(), properties.getGenericMailingSubject(), properties.getGenericMailingContent(), true);
        } catch (Exception ex) {
            String message = errorPasser.getMessagesAsString();
            if (message.isEmpty()) message = ex.getMessage();
            return new ResponseEntity<>(message, HttpStatus.NOT_ACCEPTABLE);
        }
        return new ResponseEntity<>("Zlecenie zostało zapisane.", HttpStatus.OK);
    }
}
