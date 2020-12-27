package org.jruchel.carworkshop.utils;

import org.jruchel.carworkshop.entities.Client;
import org.jruchel.carworkshop.entities.Order;
import org.jruchel.carworkshop.services.ClientService;
import org.jruchel.carworkshop.utils.random.MyRandom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@Component
public class Initializer {
    @Autowired
    private ClientService clientService;

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
        createRandomClients(MyRandom.getRandom(50, 100)).forEach(c -> clientService.save(c));
    }
}
