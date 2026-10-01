package com.eventify.web;

import com.eventify.model.VenueModel;
import com.eventify.service.VenueService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(VenueWebController.class)
public class VenueWebControllerTest {
    @Autowired
    private org.springframework.test.web.servlet.MockMvc mockMvc;

    @MockitoBean
    private VenueService venueService;

    @Test
    void listarLugares_deberiaRetornar200YVistaCorrecta() throws Exception {
        VenueModel venue= new VenueModel(1L, "Estadio Edgar Renteria", "Barranquilla", 200);
        Page<VenueModel> pagina = new PageImpl<>(List.of(venue), PageRequest.of(0, 10), 1);

        when(venueService.findAllVenues(org.mockito.ArgumentMatchers.any())).thenReturn(pagina);

        mockMvc.perform(get("/admin/venue"))
                .andExpect(status().isOk())
                .andExpect(view().name("venue/List"))
                .andExpect(model().attributeExists("venue"))
                .andExpect(model().attributeExists("paginator"));
    }

    @Test
    void listarEventos_sinEventos_deberiaMostrarModeloVacio() throws Exception {
        Page<VenueModel> paginaVacia = new PageImpl<>(List.of(), PageRequest.of(0, 10), 0);
        when(venueService.findAllVenues(org.mockito.ArgumentMatchers.any())).thenReturn(paginaVacia);

        mockMvc.perform(get("/admin/venue"))
                .andExpect(status().isOk())
                .andExpect(view().name("venue/List"))
                .andExpect(model().attribute("venue", List.of()));
    }

    @Test
    void formularioNuevoEvento_deberiaRetornar200YVistaForm() throws Exception {
        mockMvc.perform(get("/admin/venue/nuevo"))
                .andExpect(status().isOk())
                .andExpect(view().name("venue/Form"))
                .andExpect(model().attributeExists("venue"));
    }

    @Test
    void verDetalleLugar_deberiaRetornar200YVistaDetail() throws Exception {
        VenueModel venue = new VenueModel(1L, "Estadio Edgar Renteria", "Barranquilla", 200);
        when(venueService.findById(1L)).thenReturn(venue);

        mockMvc.perform(get("/admin/venue/buscar").param("id", "1"))
                .andExpect(status().isOk())
                .andExpect(view().name("venue/Detail"))
                .andExpect(model().attribute("venue", venue));
    }
}
