package com.eventify.model;

import lombok.*;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter

public class EventsModel {
    private Long id;
    private String nombre;
    private LocalDate fecha;
    private String descripcion;
}
