package com.eventify.service;

import com.eventify.exception.DuplicateException;
import com.eventify.exception.EmptyException;
import com.eventify.model.EventsModel;
import com.eventify.repository.EventsRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EventsServiceTest {

    @Mock
    private EventsRepository eventsRepository;

    @InjectMocks
    private EventsService eventsService;

    @Test
    void debeCrearEventoValido() {
        EventsModel event = new EventsModel(null, "Conferencia", LocalDate.of(2026, 10, 15), "Descripción");

        when(eventsRepository.existsById(null)).thenReturn(false);
        when(eventsRepository.existsByNameAndByFecha("Conferencia", LocalDate.of(2026, 10, 15))).thenReturn(false);
        when(eventsRepository.save(any(EventsModel.class))).thenReturn(event);

        EventsModel resultado = eventsService.createEvents(event);

        assertNotNull(resultado);
        assertEquals("Conferencia", resultado.getNombre());
        verify(eventsRepository, times(1)).save(any(EventsModel.class));
    }

    @Test
    void debeLanzarExcepcionSiIdEsMenorOIgualACero() {
        EventsModel event = new EventsModel(0L, "Conferencia", LocalDate.of(2026, 10, 15), "Descripción");

        assertThrows(EmptyException.class, () -> eventsService.createEvents(event));
        verify(eventsRepository, never()).save(any());
    }

    @Test
    void debeLanzarExcepcionSiIdYaExiste() {
        EventsModel event = new EventsModel(1L, "Conferencia", LocalDate.of(2026, 10, 15), "Descripción");

        when(eventsRepository.existsById(1L)).thenReturn(true);

        assertThrows(DuplicateException.class, () -> eventsService.createEvents(event));
        verify(eventsRepository, never()).save(any());
    }

    @Test
    void debeLanzarExcepcionSiNombreEsVacio() {
        EventsModel event = new EventsModel(null, "", LocalDate.of(2026, 10, 15), "Descripción");

        when(eventsRepository.existsById(null)).thenReturn(false);

        assertThrows(EmptyException.class, () -> eventsService.createEvents(event));
        verify(eventsRepository, never()).save(any());
    }

    @Test
    void debeLanzarExcepcionSiDescripcionEsVacia() {
        EventsModel event = new EventsModel(null, "Conferencia", LocalDate.of(2026, 10, 15), "");

        when(eventsRepository.existsById(null)).thenReturn(false);

        assertThrows(EmptyException.class, () -> eventsService.createEvents(event));
        verify(eventsRepository, never()).save(any());
    }

    @Test
    void debeLanzarExcepcionSiFechaEsNula() {
        EventsModel event = new EventsModel(null, "Conferencia", null, "Descripción");

        when(eventsRepository.existsById(null)).thenReturn(false);

        assertThrows(EmptyException.class, () -> eventsService.createEvents(event));
        verify(eventsRepository, never()).save(any());
    }

    @Test
    void debeLanzarExcepcionSiNombreYFechaDuplicados() {
        EventsModel event = new EventsModel(null, "Conferencia", LocalDate.of(2026, 10, 15), "Descripción");

        when(eventsRepository.existsById(null)).thenReturn(false);
        when(eventsRepository.existsByNameAndByFecha("Conferencia", LocalDate.of(2026, 10, 15))).thenReturn(true);

        assertThrows(DuplicateException.class, () -> eventsService.createEvents(event));
        verify(eventsRepository, never()).save(any());
    }

    @Test
    void debeRetornarListaVaciaSiNoHayEventos() {
        when(eventsRepository.findAll()).thenReturn(List.of());

        List<EventsModel> resultado = eventsService.findAllEvents();

        assertTrue(resultado.isEmpty());
    }

    @Test
    void debeActualizarEventoValido() {
        EventsModel event = new EventsModel(1L, "Conferencia Actualizada", LocalDate.of(2026, 11, 1), "Nueva descripción");

        when(eventsRepository.existsById(1L)).thenReturn(true);
        when(eventsRepository.existsByNameAndByFechaExcludingId("Conferencia Actualizada", LocalDate.of(2026, 11, 1), 1L))
                .thenReturn(false);

        EventsModel resultado = eventsService.update(event);

        assertEquals("Conferencia Actualizada", resultado.getNombre());
        verify(eventsRepository, times(1)).update(event);
    }

    @Test
    void debeLanzarExcepcionAlActualizarSiIdNoExiste() {
        EventsModel event = new EventsModel(99L, "Conferencia", LocalDate.of(2026, 10, 15), "Descripción");

        when(eventsRepository.existsById(99L)).thenReturn(false);

        assertThrows(EmptyException.class, () -> eventsService.update(event));
        verify(eventsRepository, never()).update(any());
    }

    @Test
    void debeEliminarEventoValido() {
        when(eventsRepository.existsById(1L)).thenReturn(true);

        eventsService.deleteEvents(1L);

        verify(eventsRepository, times(1)).deleteById(1L);
    }

    @Test
    void debeLanzarExcepcionAlEliminarSiIdEsNulo() {
        assertThrows(EmptyException.class, () -> eventsService.deleteEvents(null));
        verify(eventsRepository, never()).deleteById(any());
    }

    @Test
    void debeLanzarExcepcionAlEliminarSiIdNoExiste() {
        when(eventsRepository.existsById(99L)).thenReturn(false);

        assertThrows(EmptyException.class, () -> eventsService.deleteEvents(99L));
        verify(eventsRepository, never()).deleteById(any());
    }
}