package org.launchcode.codingevents.controllers;

import org.launchcode.codingevents.data.EventCategoryRepository;
import org.launchcode.codingevents.data.EventRepository;
import org.launchcode.codingevents.models.Event;
import org.launchcode.codingevents.models.EventCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;


@Controller
@RequestMapping("events")
public class EventController {

    @Autowired      // Specifies that SpringBoot should auto-populate this field ("dependency injection")
    private EventRepository eventRepository;

    @Autowired
    private EventCategoryRepository eventCategoryRepository;

    @GetMapping
    public String displayEvents(@RequestParam(required = false) Integer categoryId, Model model) {

        if (categoryId == null) {               // Checks if categoryId exists/isn't null.
            model.addAttribute("title", "All Events");
            model.addAttribute("events", eventRepository.findAll()); // If null, shows all the events.
        } else {                                // If not null, then it checks if it is linked to an id or not.
           Optional<EventCategory> result = eventCategoryRepository.findById(categoryId);
               if (result.isEmpty()) {          // If not linked, it shows "Invalid" with the given categoryId.
                   model.addAttribute("title", "Invalid Category ID: " + categoryId);
               } else {                         // If it IS linked, it shows all events linked to that categoryId.
                   EventCategory category = result.get();
                   model.addAttribute("title", "Events in category: " + category.getName());
                   model.addAttribute("events", category.getEvents());
               }
        }
        return "events/index";
    }

    // Lives at /events/create
    @GetMapping("create")
    public String displayCreateEventForm(Model model) {
        model.addAttribute("title", "Create Event");
        model.addAttribute(new Event());
        model.addAttribute("categories", eventCategoryRepository.findAll());
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

    @GetMapping("edit/{eventId}")
    public String displayEditForm(Model model, @PathVariable int eventId) {
        Optional<Event> optEvent = eventRepository.findById(eventId);

        if (optEvent.isPresent()) {
            Event event = optEvent.get();
            String title = "Edit Event " + event.getName() + " (id=" + event.getId() + ")";
            model.addAttribute("event", event);
            model.addAttribute("title", title);
            return "events/edit";
        }
        return "redirect:";
    }

    @PostMapping("edit")
    public String processEditForm(int eventId, String name, String description) {
        Optional<Event> optEvent = eventRepository.findById(eventId);

        if (optEvent.isEmpty()) {
            return "events/edit";
        }
        Event event = optEvent.get();
        event.setName(name);
        event.getEventDetails().setDescription(description);
        eventRepository.save(event);
        return "redirect:";
    }

}
