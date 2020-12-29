package org.jruchel.carworkshop.startup;

import org.jruchel.carworkshop.configuration.Properties;
import org.jruchel.carworkshop.entities.Client;
import org.jruchel.carworkshop.entities.Order;
import org.jruchel.carworkshop.entities.Role;
import org.jruchel.carworkshop.entities.User;
import org.jruchel.carworkshop.exceptions.EntityIntegrityException;
import org.jruchel.carworkshop.services.ClientService;
import org.jruchel.carworkshop.services.RoleService;
import org.jruchel.carworkshop.services.SecurityService;
import org.jruchel.carworkshop.utils.MyRandom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.*;

@Component
public class Initializer {
    @Autowired
    private ClientService clientService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private SecurityService securityService;

    private List<Client> createRandomClients(int amount) {
        List<Client> list = new ArrayList<>();

        for (int i = 0; i < amount; i++) {
            Client random = createRandomUser();
            if (list.stream().anyMatch(c -> c.getEmail().equals(random.getEmail()))) i--;
            else {
                list.add(random);
            }
        }
        return list;
    }

    private void createRoles() {
        createRole("moderator");
    }

    private void createRole(String title) {
        if (roleService.getRoleByTitle(title) == null) {
            Role moderator = new Role();
            moderator.setTitle(title);
            roleService.save(moderator);
        }
    }

    private void createModerator(String username, String password) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.grantRole(roleService.getRoleByTitle("moderator"));
        try {
            securityService.register(user);
        } catch (EntityIntegrityException e) {
            System.out.println(e.getMessage());
        }
    }

    private Client createRandomUser() {
        Client client = new Client();
        client.setPhoneNumber(MyRandom.getRandomStringWithCharsMatching("\\d", 9));
        StringBuilder email = new StringBuilder();
        email.append(MyRandom.getRandomString(MyRandom.getRandom(3, 10)));
        email.append("@");
        email.append(MyRandom.getRandomString(MyRandom.getRandom(3, 5)));
        email.append(".").append(MyRandom.getRandomString(3));
        client.setEmail(email.toString().toLowerCase(Locale.ROOT));
        for (int i = 0; i < MyRandom.getRandom(1, 3); i++) {
            Order order = new Order();
            order.setResponed(false);
            order.setDescription(MyRandom.getRandomString(MyRandom.getRandom(10, 20)));
            order.setDate(new Date());
            order.setClient(client);
            client.addOrder(order);
        }
        return client;
    }

    @PostConstruct
    private void initialize() {
        createRoles();
    }
}
