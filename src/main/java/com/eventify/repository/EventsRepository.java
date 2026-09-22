package com.eventify.repository;

import com.eventify.model.EventsModel;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;



@Repository
public interface EventsRepository extends JpaRepository<EventsModel, Long> {
    Page<EventsModel> findByNombreContaining(String nombre, Pageable pageable);
    boolean existsByNombreAndFecha(String nombre, LocalDate fecha);
    boolean existsByNombreAndFechaAndIdNot(String nombre, LocalDate fecha, Long id);
}
