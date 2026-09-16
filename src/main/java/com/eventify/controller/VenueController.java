package com.eventify.controller;

import com.eventify.model.VenueModel;
import com.eventify.service.VenueService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Lugares", description = "Gestion del catalogo de lugares")
@RestController
@RequestMapping("/api/Venue")
public class VenueController {

    private final VenueService venueService;

    public VenueController(VenueService venueService) {
        this.venueService = venueService;
    }

    @PostMapping("/create")
    public ResponseEntity<VenueModel> CreateVenue(@RequestBody VenueModel venue) {
        VenueModel created = venueService.createVenue(venue);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping
    public ResponseEntity<List<VenueModel>> findAllVenue(){
        return ResponseEntity.ok(venueService.findAllVenues());
    }

    @GetMapping("/buscar")
    public  ResponseEntity<List<VenueModel>> findByDireccion(@RequestParam String direccion){
        return ResponseEntity.ok(venueService.findByDireccion(direccion));
    }

    @PutMapping
    public ResponseEntity<VenueModel> UpdateVenue(@RequestBody VenueModel venue) {
        VenueModel updated = venueService.updateVenue(venue);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> DeleteVenue(@PathVariable Long id) {
        venueService.deleteVenue(id);
        return ResponseEntity.noContent().build();
    }
}
