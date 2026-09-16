package com.eventify.controller;

import com.eventify.model.EventsModel;
import com.eventify.service.EventsService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Tag(name = "Eventos", description = "Gestion del catalogo de eventos")
@RestController
@RequestMapping("/api/events")
public class EventsController {

    private final EventsService eventsService;

    public EventsController(EventsService eventsService) {
        this.eventsService = eventsService;
    }
    @PostMapping
    public ResponseEntity<EventsModel> createEvent(@RequestBody EventsModel event) {
        EventsModel created = eventsService.createEvents(event);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
    @GetMapping
    public ResponseEntity<List<EventsModel>> getAllEvents() {
        return ResponseEntity.ok(eventsService.findAllEvents());
    }

    @GetMapping("/buscar")
    public ResponseEntity <List<EventsModel>> findByFecha(@RequestParam LocalDate fecha) {
        return ResponseEntity.ok(eventsService.findByFecha(fecha));
    }

    @PutMapping
    public ResponseEntity<EventsModel> updateEvent(@RequestBody EventsModel event) {
        EventsModel updated = eventsService.update(event);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long id) {
        eventsService.deleteEvents(id);
        return ResponseEntity.noContent().build();
    }
}
