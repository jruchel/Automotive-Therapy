package org.jruchel.carworkshop.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.jruchel.carworkshop.models.entities.Client;
import org.jruchel.carworkshop.models.entities.Order;

@Getter
@Setter
@AllArgsConstructor
public class ClientOrderPair {
    private Client client;
    private Order order;
}
