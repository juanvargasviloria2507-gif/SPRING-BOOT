package com.eventify.service;

import com.eventify.exception.DuplicateException;
import com.eventify.exception.EmptyException;
import com.eventify.exception.FutureException;
import com.eventify.exception.NotFoundException;
import com.eventify.model.EventsModel;
import com.eventify.repository.EventsRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class EventsService {
    private final EventsRepository eventsRepository;

    public EventsService(EventsRepository eventsRepository) {
        this.eventsRepository = eventsRepository;
    }
    public EventsModel createEvents(EventsModel event) {
        if(event.getNombre() == null || event.getNombre().isBlank()) {
            throw new EmptyException("El nombre del evento no puede estar vacio");
        }
        if(event.getDescripcion() == null || event.getDescripcion().isBlank()) {
            throw new EmptyException("La descripcion del evento no puede estar vacia");
        }
        if(event.getFecha() == null) {
            throw new EmptyException("La fecha del evento no puede estar vacia");
        }
        if(eventsRepository.existsByNombreAndFecha( event.getNombre(), event.getFecha())) {
            throw new DuplicateException("Este evento ya tiene una fecha asignada");
        }
        if(!event.getFecha().isAfter(LocalDate.now())) {
            throw new FutureException("La fecha del evento debe ser posterior");
        }
        return eventsRepository.save(event);
    }
    public Page<EventsModel> findAllEvents(Pageable pageable) {
        return eventsRepository.findAll(pageable);
    }
    public EventsModel findById(Long id) {
        return eventsRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("No se encontró un evento con ese id: " + id));
    }
    public EventsModel update(EventsModel event) {
        if(event.getId() == null){
            throw new EmptyException("El id del evento no puede estar vacio");
        }
        if(!eventsRepository.existsById(event.getId())){
            throw new NotFoundException("No se encontro un evento con ese id: " + event.getId());
        }
        if(event.getNombre() == null || event.getNombre().isBlank()) {
            throw new EmptyException("El nombre del evento no puede estar vacio");
        }
        if(event.getDescripcion() == null || event.getDescripcion().isBlank()){
            throw new EmptyException("El descripcion del evento no puede estar vacia");
        }
        if(event.getFecha() == null) {
            throw new EmptyException("La fecha no puede estar vacia");
        }
        if(eventsRepository.existsByNombreAndFechaAndIdNot(event.getNombre(), event.getFecha(), event.getId())){
            throw new DuplicateException("Este evento ya tiene una fecha asignada");
        }
        return eventsRepository.save(event);
    }
    public void deleteEvents(Long id){
        if(id == null){
            throw new EmptyException("El id del evento a eliminar no puede estar vacio");
        }
        if(!eventsRepository.existsById(id)){
            throw new NotFoundException("No se encontro un evento con ese id: " + id);
        }
        eventsRepository.deleteById(id);
    }
}

