package org.jruchel.carworkshop.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Setter
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    @ManyToOne(cascade = CascadeType.PERSIST)
    private Client client;
    private String description;
    private boolean responded;
    @Transient
    private Date date;

}
