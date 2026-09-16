package com.eventify.model;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter

public class VenueModel {
    private Long id;
    private String nombre;
    private String direccion;
    private Long capacidad;
}
