package com.eventify.repository;

import com.eventify.model.EventsModel;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class EventsRepositoryTest {

    @Autowired
    private EventsRepository eventsRepository;

    @Test
    void debeGuardarYRecuperarUnEvento() {
        EventsModel event = new EventsModel(null, "Conferencia Spring", LocalDate.now().plusDays(10), "Descripción de prueba");

        EventsModel guardado = eventsRepository.save(event);

        assertNotNull(guardado.getId());
        assertEquals("Conferencia Spring", guardado.getNombre());
    }

    @Test
    void debeEncontrarEventosPorNombreConteniendoTexto() {
        eventsRepository.save(new EventsModel(null, "Conferencia Spring Boot", LocalDate.now().plusDays(5), "Descripción 1"));
        eventsRepository.save(new EventsModel(null, "Meetup de Java", LocalDate.now().plusDays(6), "Descripción 2"));
        eventsRepository.save(new EventsModel(null, "Conferencia de Kotlin", LocalDate.now().plusDays(7), "Descripción 3"));

        Pageable pageable = PageRequest.of(0, 10);
        Page<EventsModel> resultado = eventsRepository.findByNombreContaining("Conferencia", pageable);

        assertEquals(2, resultado.getTotalElements());
    }

    @Test
    void debeValidarExistsByFechaAndNombre() {
        LocalDate fecha = LocalDate.now().plusDays(10);
        eventsRepository.save(new EventsModel(null, "Conferencia Única", fecha, "Descripción"));

        boolean existe = eventsRepository.existsByNombreAndFecha("Conferencia Única", fecha);
        boolean noExiste = eventsRepository.existsByNombreAndFecha("Otro Evento", fecha);

        assertTrue(existe);
        assertFalse(noExiste);
    }

    @Test
    void debeValidarExistsByFechaAndNombreAndIdNotExcluyendoElPropioId() {
        LocalDate fecha = LocalDate.now().plusDays(10);
        EventsModel guardado = eventsRepository.save(new EventsModel(null, "Conferencia Original", fecha, "Descripción"));

        boolean existeExcluyendoseAsiMismo = eventsRepository
                .existsByNombreAndFechaAndIdNot("Conferencia Original", fecha, guardado.getId());

        assertFalse(existeExcluyendoseAsiMismo);
    }

    @Test
    void debeRetornarVacioSiNoHayEventosGuardados() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<EventsModel> resultado = eventsRepository.findAll(pageable);

        assertTrue(resultado.isEmpty());
    }
}