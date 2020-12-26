package org.jruchel.carworkshop.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.jruchel.carworkshop.validation.client.EmailConstraint;
import org.jruchel.carworkshop.validation.client.PhoneNumberConstraint;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "client")
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private int id;
    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL)
    private List<Order> orders;
    @Column(name = "phoneNumber")
    @PhoneNumberConstraint
    private String phoneNumber;
    @Column(name = "email")
    @EmailConstraint
    private String email;
    private boolean newsLetter;

    public Client() {
        this.orders = new ArrayList<>();
        this.newsLetter = false;
    }

    public void addOrder(Order order) {
        orders.add(order);
    }
}
