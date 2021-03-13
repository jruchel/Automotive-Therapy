package org.jruchel.carworkshop.models.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    @JsonIgnore
    private Client client;
    @Column(name = "description")
    private String description;
    @Column(name = "date")
    @DateConstraint
    private Date date;
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private Status status;

    public enum Status {
        pending, accepted, completed, rejected;
    }
}
