package com.eventify.model;

import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter

@Entity(name = "venue")
public class VenueModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "venue_name", unique = true, nullable = false,  length = 80)
    private String nombre;
    @Column(name = "direction", nullable = false,  length = 80)
    private String direccion;
    @Column(name = "capacity",  nullable = false)
    private Integer capacidad;
}
