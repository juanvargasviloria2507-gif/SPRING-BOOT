package com.eventify.service;

import com.eventify.exception.DuplicateException;
import com.eventify.exception.EmptyException;
import com.eventify.exception.NotFoundException;
import com.eventify.model.VenueModel;
import com.eventify.repository.VenueRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
public class VenueService {
    private final VenueRepository venueRepository;

    public VenueService(VenueRepository venueRepository) {
        this.venueRepository = venueRepository;
    }

    public VenueModel createVenue(VenueModel venue) {
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
    public Page<VenueModel> findAllVenues(Pageable pageable) {
        return venueRepository.findAll(pageable);
    }
    public VenueModel findById(Long id) {
        return venueRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("No existe un lugar con el id: " + id));
    }
    public VenueModel updateVenue(VenueModel venue){
        if(venue.getId() == null) {
            throw new EmptyException("El id del lugar no puede estar vacio");
        }
        if(!venueRepository.existsById(venue.getId())){
            throw new NotFoundException("No existe un lugar registado con el id: " + venue.getId());
        }
        if(venue.getNombre() == null || venue.getNombre().isBlank()) {
            throw new EmptyException("El nombre del lugar no puede estar vacio");
        }
        if(venueRepository.existsByNombreAndIdNot(venue.getNombre(), venue.getId())) {
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
    public void deleteVenue(Long id){
        if (id == null) {
            throw new EmptyException("El id a eliminar no puede estar vacio");
        }
        if(!venueRepository.existsById(id)) {
            throw new NotFoundException("No existe un lugar registrado con el id:" + id);
        }
        venueRepository.deleteById(id);
    }
}
