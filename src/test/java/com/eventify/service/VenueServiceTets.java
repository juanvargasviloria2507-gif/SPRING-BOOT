package com.eventify.service;

import com.eventify.exception.DuplicateException;
import com.eventify.exception.EmptyException;
import com.eventify.exception.NotFoundException;
import com.eventify.model.VenueModel;
import com.eventify.repository.VenueRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VenueServiceTest {

    @Mock
    private VenueRepository venueRepository;

    @InjectMocks
    private VenueService venueService;

    @Test
    void debeCrearVenueValido() {
        VenueModel venue = new VenueModel(null, "Teatro Test", "Barranquilla", 500);

        when(venueRepository.existsByNombre("Teatro Test")).thenReturn(false);
        when(venueRepository.save(any(VenueModel.class))).thenReturn(venue);

        VenueModel resultado = venueService.createVenue(venue);

        assertNotNull(resultado);
        assertEquals("Teatro Test", resultado.getNombre());
        verify(venueRepository, times(1)).save(any(VenueModel.class));
    }

    @Test
    void debeLanzarExcepcionSiNombreEsVacio() {
        VenueModel venue = new VenueModel(null, "", "Barranquilla", 500);

        assertThrows(EmptyException.class, () -> venueService.createVenue(venue));
        verify(venueRepository, never()).save(any());
    }

    @Test
    void debeLanzarExcepcionSiNombreYaExiste() {
        VenueModel venue = new VenueModel(null, "Teatro Existente", "Barranquilla", 500);

        when(venueRepository.existsByNombre("Teatro Existente")).thenReturn(true);

        assertThrows(DuplicateException.class, () -> venueService.createVenue(venue));
        verify(venueRepository, never()).save(any());
    }

    @Test
    void debeLanzarExcepcionSiDireccionEsVacia() {
        VenueModel venue = new VenueModel(null, "Teatro Test", "", 500);

        when(venueRepository.existsByNombre("Teatro Test")).thenReturn(false);

        assertThrows(EmptyException.class, () -> venueService.createVenue(venue));
        verify(venueRepository, never()).save(any());
    }

    @Test
    void debeLanzarExcepcionSiCapacidadEsNula() {
        VenueModel venue = new VenueModel(null, "Teatro Test", "Barranquilla", null);

        when(venueRepository.existsByNombre("Teatro Test")).thenReturn(false);

        assertThrows(EmptyException.class, () -> venueService.createVenue(venue));
        verify(venueRepository, never()).save(any());
    }

    @Test
    void debeRetornarPaginaVaciaSiNoHayVenues() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<VenueModel> paginaVacia = new PageImpl<>(List.of());

        when(venueRepository.findAll(pageable)).thenReturn(paginaVacia);

        Page<VenueModel> resultado = venueService.findAllVenues(pageable);

        assertTrue(resultado.isEmpty());
    }

    @Test
    void debeRetornarVenuePorId() {
        VenueModel venue = new VenueModel(1L, "Teatro Test", "Barranquilla", 500);

        when(venueRepository.findById(1L)).thenReturn(Optional.of(venue));

        VenueModel resultado = venueService.findById(1L);

        assertEquals("Teatro Test", resultado.getNombre());
    }

    @Test
    void debeLanzarExcepcionSiIdNoExisteAlBuscar() {
        when(venueRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> venueService.findById(99L));
    }

    @Test
    void debeActualizarVenueValido() {
        VenueModel venue = new VenueModel(1L, "Teatro Actualizado", "Barranquilla", 600);

        when(venueRepository.existsById(1L)).thenReturn(true);
        when(venueRepository.existsByNombreAndIdNot("Teatro Actualizado", 1L)).thenReturn(false);
        when(venueRepository.save(venue)).thenReturn(venue);

        VenueModel resultado = venueService.updateVenue(venue);

        assertEquals("Teatro Actualizado", resultado.getNombre());
        verify(venueRepository, times(1)).save(venue);
    }

    @Test
    void debeLanzarExcepcionAlActualizarSiIdNoExiste() {
        VenueModel venue = new VenueModel(99L, "Teatro X", "Barranquilla", 500);

        when(venueRepository.existsById(99L)).thenReturn(false);

        assertThrows(NotFoundException.class, () -> venueService.updateVenue(venue));
        verify(venueRepository, never()).save(any());
    }

    @Test
    void debeEliminarVenueValido() {
        when(venueRepository.existsById(1L)).thenReturn(true);

        venueService.deleteVenue(1L);

        verify(venueRepository, times(1)).deleteById(1L);
    }

    @Test
    void debeLanzarExcepcionAlEliminarSiIdEsNulo() {
        assertThrows(EmptyException.class, () -> venueService.deleteVenue(null));
        verify(venueRepository, never()).deleteById(any());
    }

    @Test
    void debeLanzarExcepcionAlEliminarSiIdNoExiste() {
        when(venueRepository.existsById(99L)).thenReturn(false);

        assertThrows(NotFoundException.class, () -> venueService.deleteVenue(99L));
        verify(venueRepository, never()).deleteById(any());
    }
}