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
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

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
            return new ResponseEntity<>("Ten klient jest już wpisany do bazy danych", HttpStatus.CONFLICT);
        try {
            clientService.save(client);
        } catch (Exception ex) {
            return new ResponseEntity<>(errorPasser.getMessagesAsString(), HttpStatus.NOT_ACCEPTABLE);
        }
        if (clientService.findByPhone(client.getPhoneNumber()) != null || clientService.findByEmail(client.getEmail()) != null)
            return new ResponseEntity<>("Klient został dodany do bazy danych", HttpStatus.OK);
        return new ResponseEntity<>("Błąd dowawania klienta do bazy danych", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping("/clients/unresponded")
    public ResponseEntity<List<Client>> getUnrespondedClients(@RequestParam(required = false, defaultValue = "0", value = "page") int page, @RequestParam(required = false, defaultValue = "10", value = "elements") int elements) {
        if (page < 1 && page != 0) page = 1;
        if (elements < 1) elements = 1;
        List<Client> clients = clientService.getUnrespondedClients(page, elements);
        for (Client c : clients) {
            c.setOrders(sortOrdersByDate(c.getOrders(), false));
        }
        return new ResponseEntity<>(sortClientsByDate(clients, false), HttpStatus.OK);
    }

    @GetMapping("/orders/unresponded")
    public ResponseEntity<List<Order>> getUnrespondedOrders(@RequestParam(required = false, defaultValue = "1", value = "page") int page, @RequestParam(required = false, defaultValue = "10", value = "elements") int elements) {
        if (page < 1) page = 1;
        if (elements < 1) elements = 1;
        List<Order> orders = orderService.getUnrespondedOrders(page, elements);
        orders = sortOrdersByDate(orders, false);
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    @PostMapping("/mail")
    public ResponseEntity<String> sendEmail(@RequestBody Email email) {
        try {
            email.setFrom(org.jruchel.carworkshop.utils.Properties.getInstance().readProperty("mail.sender"));
            mailingService.sendEmail(email, false);
            return new ResponseEntity<>("Wiadomość wysłana.", HttpStatus.OK);
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
    public ResponseEntity<List<Client>> getAwaitingClients(@RequestParam(required = false, defaultValue = "0", name = "page") int page, @RequestParam(required = false, defaultValue = "10", name = "elements") int elements) {
        if (page < 1 && page != 0) page = 1;
        List<Client> clients = clientService.getAwaitingClients(page, elements);
        for (Client c : clients) {
            c.setOrders(sortOrdersByDate(c.getOrders(), false));
        }
        return new ResponseEntity<>(sortClientsByDate(clients, false), HttpStatus.OK);
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

    private List<Order> sortOrdersByDate(List<Order> orders, boolean descending) {
        return orders.stream().sorted((o1, o2) -> {
            int multiplier = descending ? -1 : 1;
            return multiplier * o1.getDate().compareTo(o2.getDate());
        }).collect(Collectors.toList());
    }

    /**
     * Sorts the given list of clients by each of the clients' first order on the list
     * It's recommended to use this method in conjunction with sortOrdersByDate so that both clients and their orders display in the desired order
     *
     * @param clients    clients to sort
     * @param descending wether or not in descending order
     * @return The same list of clients, sorted
     */
    private List<Client> sortClientsByDate(List<Client> clients, boolean descending) {
        return clients.stream().sorted((o1, o2) -> {
            int multiplier = descending ? -1 : 1;
            Order order1 = getOrderDateExtremum(o1.getOrders(), descending);
            if (order1 == null) order1 = o1.getOrders().get(0);
            Order order2 = getOrderDateExtremum(o2.getOrders(), descending);
            if (order2 == null) order2 = o2.getOrders().get(0);
            return multiplier * order1.getDate().compareTo(order2.getDate());
        }).collect(Collectors.toList());
    }

    private Order getOrderDateExtremum(List<Order> orders, boolean highest) {
        if (highest) {
            return orders.stream().max(Comparator.comparing(Order::getDate)).orElse(null);
        } else {
            return orders.stream().min(Comparator.comparing(Order::getDate)).orElse(null);
        }
    }
}
