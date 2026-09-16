package com.eventify.repository;

import com.eventify.model.VenueModel;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicLong;


@Repository
public class VenueRepository {

    private final List<VenueModel> venues = new ArrayList<>() ;
    private final AtomicLong counterId = new AtomicLong(1);

    public VenueModel save(VenueModel venue) {
        if (venue.getId() == null) {
        venue.setId(counterId.getAndIncrement());
        }else {
            if(venue.getId() >= counterId.get()) {
                counterId.set(venue.getId() + 1);
            }
        }
        venues.add(venue) ;
        return venue ;
    }
    public List<VenueModel> findAll(){
        return venues ;
    }
    public List<VenueModel> findByDireccion(String direccion){
        return venues.stream()
                .filter(e -> e.getDireccion().equals(direccion))
                .toList();
    }
    public boolean existsById(Long id){
        return venues.stream()
                .anyMatch(e -> e.getId().equals(id));
    }
    public boolean existsByNombre(String nombre) {
        return venues.stream()
                .anyMatch(v -> v.getNombre().equals(nombre));
    }
    public boolean existsByNombreExcludingId(String nombre, Long id){
        return venues.stream()
                .anyMatch(v -> v.getNombre().equals(nombre) && !v.getId().equals(id));
    }
    public void update (VenueModel venueUpdate){
        for(int i = 0; i < venues.size(); i++){
            if(venues.get(i).getId().equals(venueUpdate.getId())){
                venues.set(i, venueUpdate) ;
                return;
            }
        }
    }
    public void deleteById(Long id){
        venues.removeIf(v -> v.getId().equals(id)) ;
    }
}
