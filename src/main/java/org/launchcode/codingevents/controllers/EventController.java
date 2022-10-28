package org.launchcode.codingevents.controllers;

import org.launchcode.codingevents.data.EventRepository;
import org.launchcode.codingevents.models.Event;
import org.launchcode.codingevents.models.EventType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@Controller
@RequestMapping("events")
public class EventController {

    @Autowired      // Specifies that SpringBoot should auto-populate this field ("dependency injection")
    private EventRepository eventRepository;

    @GetMapping
    public String displayAllEvents(Model model) {
        model.addAttribute("title", "All Events");
        model.addAttribute("events", eventRepository.findAll());
        return "events/index";
    }

    // Lives at /events/create
    @GetMapping("create")
    public String displayCreateEventForm(Model model) {
        model.addAttribute("title", "Create Event");
        model.addAttribute(new Event());
        model.addAttribute("types", EventType.values());
        return "events/create";
    }

    // Lives at /events/create   ***This is ok because ^ is a Get request while v is a Post request***
    @PostMapping("create")
    public String processCreateEventForm(@ModelAttribute @Valid Event newEvent, Errors errors, Model model) {

        if(errors.hasErrors()) {
            model.addAttribute("title", "Create Event");
            return "events/create";
        }
        eventRepository.save(newEvent);
        return "redirect:";
    }

    @GetMapping("delete")
    public String displayDeleteEventForm(Model model) {
        model.addAttribute("title", "Delete Events");
        model.addAttribute("events", eventRepository.findAll());
        return "events/delete";
    }

    @PostMapping("delete")
    public String processDeleteEventsForm(@RequestParam(required = false) int[] eventIds) {

        if (eventIds != null) {
            for (int id : eventIds) {
                eventRepository.deleteById(id);
            }
        }
        return "redirect:";
    }

//    @GetMapping("edit/{eventId}")                      // No longer works without updating code for ORM
//    public String displayEditForm(Model model, @PathVariable int eventId) {
//        Event eventToEdit = EventData.getById(eventId);
//        String title = "Edit Event " + eventToEdit.getName() + " (id=" + eventToEdit.getId() + ")";
//
//        model.addAttribute("event", eventToEdit);
//        model.addAttribute("title", title);
//        return "events/edit";
//    }
//
//    @PostMapping("edit")
//    public String processEditForm(int eventId, String name, String description) {
//        Event eventToEdit = EventData.getById(eventId);
//        eventToEdit.setName(name);
//        eventToEdit.setDescription(description);
//        return "redirect:";
//    }

}
