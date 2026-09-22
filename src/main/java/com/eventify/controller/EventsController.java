package com.eventify.controller;
import com.eventify.model.EventsModel;
import com.eventify.service.EventsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Eventos", description = "Gestion del catalogo de eventos")
@RestController
@RequestMapping("/api/events")
public class EventsController {

    private final EventsService eventsService;
    public EventsController(EventsService eventsService) {
        this.eventsService = eventsService;
    }

    @PostMapping("/crear")
    @Operation(summary = "Crear un evento")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Evento creado"),
            @ApiResponse(responseCode = "400", description = "Datos invalidos o Duplicados")
    })
    public ResponseEntity<EventsModel> createEvent(@Valid @RequestBody EventsModel event) {
        EventsModel created = eventsService.createEvents(event);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
    @GetMapping
    @Operation(summary = "Listar eventos de forma paginada")
    @Parameters({
            @Parameter(name = "page", description = "numero de pagina (empieza en 0)"),
            @Parameter(name = "size", description = "Cantidad de resultados por pagina"),
            @Parameter(name = "sort", description = "Campo y direccion de orden, ej:nombre, asc")
    })
    @ApiResponse(responseCode = "200", description = "Listado paginados")
    public ResponseEntity<Page<EventsModel>> findAll(@ParameterObject @PageableDefault(size = 10)Pageable pageable) {
        return ResponseEntity.ok(eventsService.findAllEvents(pageable));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar evento por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Evento encontrado"),
            @ApiResponse(responseCode = "404", description = "No se encontro un evento con ese ID")
    })
    public ResponseEntity <EventsModel> getById(@PathVariable Long id) {
        return ResponseEntity.ok(eventsService.findById(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar un evento existente")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Evento actualizado correctamente"),
                    @ApiResponse(responseCode = "400", description = "Datos invalidos o duplicados"),
                    @ApiResponse(responseCode = "404", description = "No se encontro un evento con ese ID")
            }
    )
    public ResponseEntity<EventsModel> updateEvent(@PathVariable Long id, @Valid @RequestBody EventsModel event) {
        event.setId(id);
        return ResponseEntity.ok(eventsService.update(event));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un eveto existente")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "204", description = "Evento eliminado correctamente"),
                    @ApiResponse(responseCode = "404", description = "No se encontro un evento con ese ID")
            }
    )
    public ResponseEntity<Void> deleteEvent(@PathVariable Long id) {
        eventsService.deleteEvents(id);
        return ResponseEntity.noContent().build();
    }
}
