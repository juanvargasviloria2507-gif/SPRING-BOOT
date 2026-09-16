package com.eventify.repository;

import com.eventify.model.EventsModel;

import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicLong;


@Repository
public class EventsRepository {
    private final List<EventsModel> events = new ArrayList<>() ;
    private final AtomicLong counterId = new AtomicLong(1);

    public EventsModel save(EventsModel event){
        if (event.getId() == null) {
            event.setId(counterId.getAndIncrement());
        } else {
            // Si mandan un id manual, aseguramos que el contador nunca "se quede atrás"
            if (event.getId() >= counterId.get()) {
                counterId.set(event.getId() + 1);
            }
        }
        events.add(event) ;
        return event ;
    }
    public List<EventsModel> findAll(){
        return events ;
    }
    public List<EventsModel> findByFecha(LocalDate fecha){
        return events.stream()
                .filter(e -> e.getFecha().equals(fecha))
                .toList();
    }
    public boolean existsByNameAndByFecha(String nombre, LocalDate fecha){
        return events.stream()
                .anyMatch(
                        e -> e.getNombre()
                        .equals(nombre)
                        && e.getFecha()
                        .equals(fecha));
    }
    public boolean existsById(Long id){
        return events.stream()
                .anyMatch(e -> e.getId()
                .equals(id));
    }
    public boolean existsByNameAndByFechaExcludingId(String nombre, LocalDate fecha, Long id){
        return events.stream().anyMatch(e -> e.getNombre().equals(nombre)
                && e.getFecha().equals(fecha)
                && !e.getId().equals(id));
    }
    public void update (EventsModel eventUpdate){
        for(int i = 0; i < events.size(); i++){
            if(events.get(i).getId().equals(eventUpdate.getId())){
                events.set(i, eventUpdate) ;
                return;
            }
        }
    }
    public void deleteById (Long id){
        events.removeIf(e -> e.getId().equals(id)) ;
    }
}
