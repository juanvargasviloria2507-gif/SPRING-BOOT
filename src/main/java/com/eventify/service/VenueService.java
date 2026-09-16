package com.eventify.service;

import com.eventify.exception.DuplicateException;
import com.eventify.exception.EmptyException;
import com.eventify.model.VenueModel;
import com.eventify.repository.VenueRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VenueService {
    private final VenueRepository venueRepository;

    public VenueService(VenueRepository venueRepository) {
        this.venueRepository = venueRepository;
    }

    public VenueModel createVenue(VenueModel venue) {
        if (venue.getId() != null && venue.getId() <= 0) {
            throw new EmptyException("El id del lugar tiene que ser mayor a cero");
        }
        if (venueRepository.existsById(venue.getId())) {
            throw new DuplicateException("El lugar ya existe");
        }
        if(venue.getNombre() == null || venue.getNombre().isBlank()) {
            throw new EmptyException("El nombre del lugar no puede estar vacio");
        }
        if(venueRepository.existsByNombre(venue.getNombre())) {
            throw new DuplicateException("El nombre del lugar ya existe");
        }
        if(venue.getDireccion() == null || venue.getDireccion().isBlank()) {
            throw new EmptyException("La direccion del lugar no puede estar vacia");
        }
        if (venue.getCapacidad() == null){
            throw new EmptyException("La capacidad no puede estar vacia");
        }
        return venueRepository.save(venue);
    }
    public List<VenueModel> findAllVenues() {
        return venueRepository.findAll();
    }
    public List<VenueModel> findByDireccion(String direccion) {
        return venueRepository.findByDireccion(direccion);
    }
    public VenueModel updateVenue(VenueModel venue){
        if(venue.getId() == null) {
            throw new EmptyException("El id del lugar no puede estar vacio");
        }
        if(!venueRepository.existsById(venue.getId())){
            throw new EmptyException("No existe un lugar registado con ese id");
        }
        if(venue.getNombre() == null || venue.getNombre().isBlank()) {
            throw new EmptyException("El nombre del lugar no puede estar vacio");
        }
        if(venueRepository.existsByNombreExcludingId(venue.getNombre(), venue.getId())) {
            throw new DuplicateException("El nombre del lugar ya existe");
        }
        if(venue.getDireccion() == null || venue.getDireccion().isBlank()) {
            throw new EmptyException("La direccion del lugar no puede estar vacia");
        }
        if (venue.getCapacidad() == null){
            throw new EmptyException("La capacidad no puede estar vacia");
        }
        venueRepository.update(venue);
        return venue;
    }
    public void deleteVenue(Long id){
        if (id == null) {
            throw new EmptyException("El id a eliminar no puede estar vacio");
        }
        if(!venueRepository.existsById(id)) {
            throw new EmptyException("No existe un lugar registrado con ese id");
        }
        venueRepository.deleteById(id);
    }
}
