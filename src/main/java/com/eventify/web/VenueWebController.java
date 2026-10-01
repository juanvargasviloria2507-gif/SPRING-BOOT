package com.eventify.web;

import com.eventify.model.VenueModel;
import com.eventify.service.VenueService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/venue")
public class VenueWebController {
    private VenueService venueService;

    public VenueWebController(VenueService venueService) {
        this.venueService = venueService;
    }

    @PostMapping("/crear")
    public String create(@ModelAttribute VenueModel venueModel, RedirectAttributes flash) {
        try {
            venueService.createVenue(venueModel);
            flash.addFlashAttribute("message", "Lugar creado exitosamente");
        }catch (RuntimeException ex){
            flash.addFlashAttribute("error", ex.getMessage());
        }
        return "redirect:/admin/venue";
    }

    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        model.addAttribute("venue", new VenueModel());
        return "venue/Form";
    }

    @GetMapping
    public String venue(@RequestParam (defaultValue = "0") int page,
                        @RequestParam (defaultValue = "10") int size,
                        Model model) {
        Pageable pageable = PageRequest.of(page, size);
        Page<VenueModel>  pagina = venueService.findAllVenues(pageable);
        model.addAttribute("venue", pagina.getContent());
        model.addAttribute("paginator", pagina);
        return "venue/List";
    }

    @GetMapping("/buscar")
    public String getById(@RequestParam("id") Long id, Model model, RedirectAttributes flash) {
        try {
            model.addAttribute("venue", venueService.findById(id));
            return "venue/Detail";
        }catch (RuntimeException ex){
            flash.addFlashAttribute("error", ex.getMessage());
        }
        return "redirect:/admin/venue";
    }

    @GetMapping("/{id}/editar")
    public String editForm(@PathVariable("id") Long id, Model model) {
        model.addAttribute("venue", venueService.findById(id));
        return "venue/Form";
    }

    @PostMapping("/{id}/actualizar")
    public String update(@PathVariable Long id, @ModelAttribute("venue") VenueModel venue, RedirectAttributes flash) {
        try {
            venue.setId(id);
            venueService.updateVenue(venue);
            flash.addFlashAttribute("message", "Lugar modificado con exito");
        }catch (RuntimeException ex){
            flash.addFlashAttribute("error", ex.getMessage());
        }
        return "redirect:/admin/venue";
    }

    @PostMapping("/{id}/eliminar")
    public String delete(@PathVariable("id") Long id, RedirectAttributes flash) {
        try {
            venueService.deleteVenue(id);
            flash.addFlashAttribute("message", "Lugar eliminado con exito");
        }catch (RuntimeException ex){
            flash.addFlashAttribute("error", ex.getMessage());
        }
        return "redirect:/admin/venue";
    }
}
