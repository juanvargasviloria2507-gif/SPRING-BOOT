package com.eventify.web;

import com.eventify.model.EventsModel;
import com.eventify.service.EventsService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/events")
public class EventsWebController {

    private final EventsService eventsService;

    public EventsWebController(EventsService eventsService) {
        this.eventsService = eventsService;
    }

    @PostMapping("/crear")
    public String create(@ModelAttribute("event")EventsModel event, RedirectAttributes flash) {
        try{
            eventsService.createEvents(event);
            flash.addFlashAttribute("message", "Evento creado con exito");
        }catch (RuntimeException ex){
            flash.addFlashAttribute("error", ex.getMessage());
        }
        return "redirect:/admin/events";
    }

    @GetMapping("/nuevo")
    public String newEvent(Model model) {
        model.addAttribute("event", new EventsModel());
        return "events/Form";
    }

    @GetMapping
    public String listEvents(@RequestParam(defaultValue = "0") int page,
                             @RequestParam(defaultValue = "10") int size,
                             Model model) {
        Pageable pageable = PageRequest.of(page, size);
        Page<EventsModel> pagina = eventsService.findAllEvents(pageable);
        model.addAttribute("events", pagina.getContent());
        model.addAttribute("paginator", pagina);
        return "events/List";
    }

    @GetMapping("/buscar")
    public String getById(@RequestParam("id") Long id, Model model, RedirectAttributes flash) {
        try {
            model.addAttribute("event", eventsService.findById(id));
            return "events/Detail";
        }catch (RuntimeException ex){
            flash.addAttribute("error", ex.getMessage());
        }
        return "redirect:/admin/events";
    }

    @GetMapping("/{id}/editar")
    public String editForm(@PathVariable("id") Long id, Model model) {
        model.addAttribute("event", eventsService.findById(id));
        return "events/Form";
    }

    @PostMapping("/{id}/actualizar")
    public String update(@PathVariable Long id, @ModelAttribute("event") EventsModel event, RedirectAttributes flash) {
        try {
            event.setId(id);
            eventsService.update(event);
            flash.addFlashAttribute("message", "Evento modificado con exito");
        }catch (RuntimeException ex){
            flash.addFlashAttribute("error", ex.getMessage());
        }
        return "redirect:/admin/events";
    }

    @PostMapping("/{id}/eliminar")
    public String deleteEvent(@PathVariable("id") Long id, RedirectAttributes flash) {
        try {
            eventsService.deleteEvents(id);
            flash.addFlashAttribute("message", "Evento eliminado con exito");
        }catch (RuntimeException ex){
            flash.addFlashAttribute("error", ex.getMessage());
        }
        return "redirect:/admin/events";
    }
}
