package com.eventify.web;

import com.eventify.model.EventsModel;
import com.eventify.service.EventsService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EventsWebController.class)
class EventsWebControllerTest {

    @Autowired
    private org.springframework.test.web.servlet.MockMvc mockMvc;

    @MockitoBean
    private EventsService eventsService;

    @Test
    void listarEventos_deberiaRetornar200YVistaCorrecta() throws Exception {
        EventsModel evento = new EventsModel(1L, "Conferencia Spring", LocalDate.now().plusDays(10), "Descripción");
        Page<EventsModel> pagina = new PageImpl<>(List.of(evento), PageRequest.of(0, 10), 1);

        when(eventsService.findAllEvents(org.mockito.ArgumentMatchers.any())).thenReturn(pagina);

        mockMvc.perform(get("/admin/events"))
                .andExpect(status().isOk())
                .andExpect(view().name("events/List"))
                .andExpect(model().attributeExists("events"))
                .andExpect(model().attributeExists("paginator"));
    }

    @Test
    void listarEventos_sinEventos_deberiaMostrarModeloVacio() throws Exception {
        Page<EventsModel> paginaVacia = new PageImpl<>(List.of(), PageRequest.of(0, 10), 0);
        when(eventsService.findAllEvents(org.mockito.ArgumentMatchers.any())).thenReturn(paginaVacia);

        mockMvc.perform(get("/admin/events"))
                .andExpect(status().isOk())
                .andExpect(view().name("events/List"))
                .andExpect(model().attribute("events", List.of()));
    }

    @Test
    void formularioNuevoEvento_deberiaRetornar200YVistaForm() throws Exception {
        mockMvc.perform(get("/admin/events/nuevo"))
                .andExpect(status().isOk())
                .andExpect(view().name("events/Form"))
                .andExpect(model().attributeExists("event"));
    }

    @Test
    void verDetalleEvento_deberiaRetornar200YVistaDetail() throws Exception {
        EventsModel evento = new EventsModel(1L, "Conferencia Spring", LocalDate.now().plusDays(10), "Descripción");
        when(eventsService.findById(1L)).thenReturn(evento);

        mockMvc.perform(get("/admin/events/buscar").param("id", "1"))
                .andExpect(status().isOk())
                .andExpect(view().name("events/Detail"))
                .andExpect(model().attribute("event", evento));
    }
}
