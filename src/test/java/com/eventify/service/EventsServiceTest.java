package com.eventify.service;

import com.eventify.exception.DuplicateException;
import com.eventify.exception.EmptyException;
import com.eventify.exception.FutureException;
import com.eventify.exception.NotFoundException;
import com.eventify.model.EventsModel;
import com.eventify.repository.EventsRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

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
        EventsModel event = new EventsModel(null, "Conferencia", LocalDate.now().plusDays(10), "Descripción");

        when(eventsRepository.existsByNombreAndFecha(event.getNombre(), event.getFecha())).thenReturn(false);
        when(eventsRepository.save(any(EventsModel.class))).thenReturn(event);

        EventsModel resultado = eventsService.createEvents(event);

        assertNotNull(resultado);
        assertEquals("Conferencia", resultado.getNombre());
        verify(eventsRepository, times(1)).save(any(EventsModel.class));
    }

    @Test
    void debeLanzarExcepcionSiNombreEsVacio() {
        EventsModel event = new EventsModel(null, "", LocalDate.now().plusDays(10), "Descripción");

        assertThrows(EmptyException.class, () -> eventsService.createEvents(event));
        verify(eventsRepository, never()).save(any());
    }

    @Test
    void debeLanzarExcepcionSiDescripcionEsVacia() {
        EventsModel event = new EventsModel(null, "Conferencia", LocalDate.now().plusDays(10), "");

        assertThrows(EmptyException.class, () -> eventsService.createEvents(event));
        verify(eventsRepository, never()).save(any());
    }

    @Test
    void debeLanzarExcepcionSiFechaEsNula() {
        EventsModel event = new EventsModel(null, "Conferencia", null, "Descripción");

        assertThrows(EmptyException.class, () -> eventsService.createEvents(event));
        verify(eventsRepository, never()).save(any());
    }

    @Test
    void debeLanzarExcepcionSiFechaNoEsFutura() {
        EventsModel event = new EventsModel(null, "Conferencia", LocalDate.now().minusDays(1), "Descripción");

        assertThrows(FutureException.class, () -> eventsService.createEvents(event));
        verify(eventsRepository, never()).save(any());
    }

    @Test
    void debeLanzarExcepcionSiNombreYFechaDuplicados() {
        LocalDate fecha = LocalDate.now().plusDays(10);
        EventsModel event = new EventsModel(null, "Conferencia", fecha, "Descripción");

        when(eventsRepository.existsByNombreAndFecha("Conferencia", fecha)).thenReturn(true);

        assertThrows(DuplicateException.class, () -> eventsService.createEvents(event));
        verify(eventsRepository, never()).save(any());
    }

    @Test
    void debeRetornarPaginaVaciaSiNoHayEventos() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<EventsModel> paginaVacia = new PageImpl<>(List.of());

        when(eventsRepository.findAll(pageable)).thenReturn(paginaVacia);

        Page<EventsModel> resultado = eventsService.findAllEvents(pageable);

        assertTrue(resultado.isEmpty());
    }

    @Test
    void debeRetornarEventoPorId() {
        EventsModel event = new EventsModel(1L, "Conferencia", LocalDate.now().plusDays(10), "Descripción");

        when(eventsRepository.findById(1L)).thenReturn(Optional.of(event));

        EventsModel resultado = eventsService.findById(1L);

        assertEquals("Conferencia", resultado.getNombre());
    }

    @Test
    void debeLanzarExcepcionSiIdNoExisteAlBuscar() {
        when(eventsRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> eventsService.findById(99L));
    }

    @Test
    void debeActualizarEventoValido() {
        LocalDate fecha = LocalDate.now().plusDays(15);
        EventsModel event = new EventsModel(1L, "Conferencia Actualizada", fecha, "Nueva descripción");

        when(eventsRepository.existsById(1L)).thenReturn(true);
        when(eventsRepository.existsByNombreAndFechaAndIdNot("Conferencia Actualizada", fecha,1L)).thenReturn(false);
        when(eventsRepository.save(event)).thenReturn(event);

        EventsModel resultado = eventsService.update(event);

        assertEquals("Conferencia Actualizada", resultado.getNombre());
        verify(eventsRepository, times(1)).save(event);
    }

    @Test
    void debeLanzarExcepcionAlActualizarSiIdNoExiste() {
        EventsModel event = new EventsModel(99L, "Conferencia", LocalDate.now().plusDays(10), "Descripción");

        when(eventsRepository.existsById(99L)).thenReturn(false);

        assertThrows(NotFoundException.class, () -> eventsService.update(event));
        verify(eventsRepository, never()).save(any());
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

        assertThrows(NotFoundException.class, () -> eventsService.deleteEvents(99L));
        verify(eventsRepository, never()).deleteById(any());
    }
}