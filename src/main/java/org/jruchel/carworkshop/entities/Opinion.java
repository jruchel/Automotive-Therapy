package org.jruchel.carworkshop.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "opinions")
@Getter
@Setter
public class Opinion {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    @Column(name = "stars")
    private int stars;
    @Column(name = "name")
    private String name;
    @Column(name = "description")
    private String description;
}
