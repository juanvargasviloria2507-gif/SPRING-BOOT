package com.eventify.service;

import com.eventify.exception.DuplicateException;
import com.eventify.exception.EmptyException;
import com.eventify.model.EventsModel;
import com.eventify.repository.EventsRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class EventsService {
    private final EventsRepository eventsRepository;

    public EventsService(EventsRepository eventsRepository) {
        this.eventsRepository = eventsRepository;
    }
    public EventsModel createEvents(EventsModel event) {
        if(event.getId() != null && event.getId() <= 0){
            throw new EmptyException("El id del evento tiene que ser mayor a cero");
        }
        if (eventsRepository.existsById(event.getId())) {
            throw new DuplicateException("El evento ya existe");
        }
        if(event.getNombre() == null || event.getNombre().isBlank()) {
            throw new EmptyException("El nombre del evento no puede estar vacio");
        }
        if(event.getDescripcion() == null || event.getDescripcion().isBlank()) {
            throw new EmptyException("La descripcion del evento no puede estar vacia");
        }
        if(event.getFecha() == null) {
            throw new EmptyException("La fecha del evento no puede estar vacia");
        }
        if(eventsRepository.existsByNameAndByFecha(event.getNombre(), event.getFecha())){
            throw new DuplicateException("Este evento ya tiene una fecha asignada");
        }
        return eventsRepository.save(event);
    }
    public List<EventsModel> findAllEvents() {
        return eventsRepository.findAll();
    }
    public List<EventsModel> findByFecha(LocalDate fecha) {
        return eventsRepository.findByFecha(fecha);
    }
    public EventsModel update(EventsModel event) {
        if(event.getId() == null){
            throw new EmptyException("El id del evento no puede estar vacio");
        }
        if(!eventsRepository.existsById(event.getId())){
            throw new EmptyException("No existe el evento con el id: " + event.getId());
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
        if(eventsRepository.existsByNameAndByFechaExcludingId(event.getNombre(), event.getFecha(), event.getId())){
            throw new DuplicateException("Este evento ya tiene una fecha asignada");
        }
        eventsRepository.update(event);
        return event;
    }
    public void deleteEvents(Long id){
        if(id == null){
            throw new EmptyException("El id del evento a eliminar no puede estar vacio");
        }
        if(!eventsRepository.existsById(id)){
            throw new EmptyException("No existe el evento con el id: " + id);
        }
        eventsRepository.deleteById(id);
    }
}

