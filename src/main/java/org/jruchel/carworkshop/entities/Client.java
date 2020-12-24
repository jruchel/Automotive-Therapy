package org.jruchel.carworkshop.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL)
    private List<Order> orderList;
    private String phoneNumber;
    private String email;
}
