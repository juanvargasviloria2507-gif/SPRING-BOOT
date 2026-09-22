package com.eventify.repository;

import com.eventify.model.VenueModel;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class VenueRepositoryTest {

    @Autowired
    private VenueRepository venueRepository;

    @Test
    void debeGuardarYRecuperarUnVenue() {
        VenueModel venue = new VenueModel(null, "Teatro de Prueba", "Barranquilla", 500);

        VenueModel guardado = venueRepository.save(venue);

        assertNotNull(guardado.getId());
        assertEquals("Teatro de Prueba", guardado.getNombre());
    }

    @Test
    void debeEncontrarVenuesPorNombreConteniendoTexto() {
        venueRepository.save(new VenueModel(null, "Teatro Amira de la Rosa", "Barranquilla", 500));
        venueRepository.save(new VenueModel(null, "Centro de Eventos", "Barranquilla", 1000));
        venueRepository.save(new VenueModel(null, "Teatro Municipal", "Cartagena", 300));

        Pageable pageable = PageRequest.of(0, 10);
        Page<VenueModel> resultado = venueRepository.findByNombreContaining("Teatro", pageable);

        assertEquals(2, resultado.getTotalElements());
    }

    @Test
    void debeValidarExistsByNombre() {
        venueRepository.save(new VenueModel(null, "Venue Único", "Barranquilla", 400));

        assertTrue(venueRepository.existsByNombre("Venue Único"));
        assertFalse(venueRepository.existsByNombre("Venue Inexistente"));
    }

    @Test
    void debeValidarExistsByNombreAndIdNotExcluyendoElPropioId() {
        VenueModel guardado = venueRepository.save(new VenueModel(null, "Venue Original", "Barranquilla", 400));

        boolean existeExcluyendoseAsiMismo = venueRepository
                .existsByNombreAndIdNot("Venue Original", guardado.getId());

        assertFalse(existeExcluyendoseAsiMismo);
    }

    @Test
    void debeRetornarVacioSiNoHayVenuesGuardados() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<VenueModel> resultado = venueRepository.findAll(pageable);

        assertTrue(resultado.isEmpty());
    }
}