package com.eventify.controller;

import com.eventify.model.VenueModel;
import com.eventify.service.EventsService;
import com.eventify.service.VenueService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Lugares", description = "Gestion del catalogo de lugares")
@RestController
@RequestMapping("/api/Venue")
public class VenueController {

    private final VenueService venueService;

    public VenueController(VenueService venueService, EventsService eventsService) {
        this.venueService = venueService;
    }

    @PostMapping("/crear")
    @Operation(summary = "Crear un lugar")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Lugar creado"),
            @ApiResponse(responseCode = "400", description = "Datos invalidos o duplicados")
    })
    public ResponseEntity<VenueModel> CreateVenue(@Valid @RequestBody VenueModel venue) {
        VenueModel created = venueService.createVenue(venue);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping
    @Operation(summary = "Listar lugares de forma paginada")
    @Parameters({
            @Parameter(name = "page", description = "numero de pagina (empieza en 0)"),
            @Parameter(name = "size", description = "Cantidad de resultados por pagina"),
            @Parameter(name = "sort", description = "Campo y direccion de orden, ej:nombre, asc")
    })
    @ApiResponse(responseCode = "200", description = "Listado paginado de eventos")
    public ResponseEntity <Page<VenueModel>> getAllVenues(@ParameterObject @PageableDefault(size = 10)Pageable pageable){
        return ResponseEntity.ok(venueService.findAllVenues(pageable));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lugar encontrado"),
            @ApiResponse(responseCode = "404", description = "No se encontro un lugar con ese ID")
    })
    public ResponseEntity<VenueModel> getById(@PathVariable Long id ){
        return ResponseEntity.ok(venueService.findById(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar un lugar")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lugar actualizado correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos invalidos o duplicados"),
            @ApiResponse(responseCode = "404", description = "No se encontro un lugar con ese ID")
    })
    public ResponseEntity<VenueModel> UpdateVenue(@PathVariable Long id, @Valid @RequestBody VenueModel venue) {
        venue.setId(id);
        return ResponseEntity.ok(venueService.updateVenue(venue));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un lugar")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lugar eliminado exitosamente"),
            @ApiResponse(responseCode = "404", description = "No se encontro un lugar con ese ID")
    })
    public ResponseEntity <Void> DeleteVenue(@PathVariable Long id) {
        venueService.deleteVenue(id);
        return ResponseEntity.noContent().build();
    }
}
