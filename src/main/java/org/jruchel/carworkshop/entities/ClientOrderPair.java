package org.jruchel.carworkshop.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ClientOrderPair {
    private Client client;
    private Order order;
}
