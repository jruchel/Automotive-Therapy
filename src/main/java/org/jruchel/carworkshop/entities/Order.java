package org.jruchel.carworkshop.entities;

import lombok.Getter;
import lombok.Setter;
import org.jruchel.carworkshop.validation.order.ClientConstraint;
import org.jruchel.carworkshop.validation.order.DateConstraint;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Setter
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private int id;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "client_id")
    @ClientConstraint
    private Client client;
    @Column(name = "responded")
    private boolean responed;
    @Column(name = "description")
    private String description;
    @Column(name = "date")
    @DateConstraint
    private Date date;

}
