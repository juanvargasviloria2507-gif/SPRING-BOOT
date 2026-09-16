package com.eventify.service;

import com.eventify.exception.DuplicateException;
import com.eventify.exception.EmptyException;
import com.eventify.model.VenueModel;
import com.eventify.repository.VenueRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

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
        VenueModel venue = new VenueModel(null, "Teatro Test", "Barranquilla", 500L);

        when(venueRepository.existsById(null)).thenReturn(false);
        when(venueRepository.existsByNombre("Teatro Test")).thenReturn(false);
        when(venueRepository.save(any(VenueModel.class))).thenReturn(venue);

        VenueModel resultado = venueService.createVenue(venue);

        assertNotNull(resultado);
        assertEquals("Teatro Test", resultado.getNombre());
        verify(venueRepository, times(1)).save(any(VenueModel.class));
    }

    @Test
    void debeLanzarExcepcionSiIdEsMenorOIgualACero() {
        VenueModel venue = new VenueModel(0L, "Teatro Test", "Barranquilla", 500L);

        assertThrows(EmptyException.class, () -> venueService.createVenue(venue));
        verify(venueRepository, never()).save(any());
    }

    @Test
    void debeLanzarExcepcionSiIdYaExiste() {
        VenueModel venue = new VenueModel(1L, "Teatro Test", "Barranquilla", 500L);

        when(venueRepository.existsById(1L)).thenReturn(true);

        assertThrows(DuplicateException.class, () -> venueService.createVenue(venue));
        verify(venueRepository, never()).save(any());
    }

    @Test
    void debeLanzarExcepcionSiNombreEsVacio() {
        VenueModel venue = new VenueModel(null, "", "Barranquilla", 500L);

        when(venueRepository.existsById(null)).thenReturn(false);

        assertThrows(EmptyException.class, () -> venueService.createVenue(venue));
        verify(venueRepository, never()).save(any());
    }

    @Test
    void debeLanzarExcepcionSiNombreYaExiste() {
        VenueModel venue = new VenueModel(null, "Teatro Existente", "Barranquilla", 500L);

        when(venueRepository.existsById(null)).thenReturn(false);
        when(venueRepository.existsByNombre("Teatro Existente")).thenReturn(true);

        assertThrows(DuplicateException.class, () -> venueService.createVenue(venue));
        verify(venueRepository, never()).save(any());
    }

    @Test
    void debeLanzarExcepcionSiDireccionEsVacia() {
        VenueModel venue = new VenueModel(null, "Teatro Test", "", 500L);

        when(venueRepository.existsById(null)).thenReturn(false);
        when(venueRepository.existsByNombre("Teatro Test")).thenReturn(false);

        assertThrows(EmptyException.class, () -> venueService.createVenue(venue));
        verify(venueRepository, never()).save(any());
    }

    @Test
    void debeLanzarExcepcionSiCapacidadEsNula() {
        VenueModel venue = new VenueModel(null, "Teatro Test", "Barranquilla", null);

        when(venueRepository.existsById(null)).thenReturn(false);
        when(venueRepository.existsByNombre("Teatro Test")).thenReturn(false);

        assertThrows(EmptyException.class, () -> venueService.createVenue(venue));
        verify(venueRepository, never()).save(any());
    }

    @Test
    void debeRetornarListaVaciaSiNoHayVenues() {
        when(venueRepository.findAll()).thenReturn(List.of());

        List<VenueModel> resultado = venueService.findAllVenues();

        assertTrue(resultado.isEmpty());
    }

    @Test
    void debeActualizarVenueValido() {
        VenueModel venue = new VenueModel(1L, "Teatro Actualizado", "Barranquilla", 600L);

        when(venueRepository.existsById(1L)).thenReturn(true);
        when(venueRepository.existsByNombreExcludingId("Teatro Actualizado", 1L)).thenReturn(false);

        VenueModel resultado = venueService.updateVenue(venue);

        assertEquals("Teatro Actualizado", resultado.getNombre());
        verify(venueRepository, times(1)).update(venue);
    }

    @Test
    void debeLanzarExcepcionAlActualizarSiIdNoExiste() {
        VenueModel venue = new VenueModel(99L, "Teatro X", "Barranquilla", 500L);

        when(venueRepository.existsById(99L)).thenReturn(false);

        assertThrows(EmptyException.class, () -> venueService.updateVenue(venue));
        verify(venueRepository, never()).update(any());
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

        assertThrows(EmptyException.class, () -> venueService.deleteVenue(99L));
        verify(venueRepository, never()).deleteById(any());
    }
}