package com.eventify.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
import lombok.*;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter

@Entity(name = "event")
@Table(name = "events", uniqueConstraints = {@UniqueConstraint(columnNames = {"event_name", "date"})})
public class EventsModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column (name = "id")
    private Long id;
    @Column (name = "event_name", nullable = false, length = 80)
    private String nombre;
    @Future (message = "La fecha del evento debe ser posterior")
    @Column (name = "date", nullable = false)
    private LocalDate fecha;
    @Column (name = "description", nullable = false, length = 150)
    private String descripcion;
}
