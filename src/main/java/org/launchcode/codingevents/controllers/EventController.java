package org.launchcode.codingevents.controllers;

import org.launchcode.codingevents.data.EventCategoryRepository;
import org.launchcode.codingevents.data.EventRepository;
import org.launchcode.codingevents.data.TagRepository;
import org.launchcode.codingevents.models.Event;
import org.launchcode.codingevents.models.EventCategory;
import org.launchcode.codingevents.models.Tag;
import org.launchcode.codingevents.models.dto.EventTagDTO;
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

    @Autowired
    private TagRepository tagRepository;

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

    @GetMapping("detail")
    public String displayEventDetails(@RequestParam Integer eventId, Model model) {
        Optional<Event> result = eventRepository.findById(eventId);

        if (result.isEmpty()) {
            model.addAttribute("title", "Invalid Event ID: " + eventId);
        } else {
            Event event = result.get();
            model.addAttribute("title", event.getName() + " Details");
            model.addAttribute("event", event);
            model.addAttribute("tags", event.getTags());
        }
        return "events/detail";
    }

    // Responds to requests /events/add-tag?eventId=13
    @GetMapping("add-tag") // Specifies that the handler will be available at the route /events/add-tag.
    public String displayAddTagForm(@RequestParam Integer eventId, Model model) {
        // Queries the repository for the Event object with ID equal to the value of eventId.
        Optional<Event> result = eventRepository.findById(eventId);
        // Extracts the Event object from the result of the query. We would ideally include a conditional to
        // check that such an object exists before proceeding, but are omitting it here to focus on DTO usage.
        Event event = result.get();
        // Creates a title for the form, which includes the name of the event.
        model.addAttribute("title", "Add Tag to " + event.getName());
        // Passes a collection of all available tags into the view. This collection will be used to render a
        // dropdown that the user can use to select the tag to be added.
        model.addAttribute("tags", tagRepository.findAll());
        // This will be used to help render the form.
        EventTagDTO eventTag = new EventTagDTO();
        // Assigns the event property of the eventTag DTO object. This will enable us to reference the specific
        // event when rendering the form, so we can assign the tag to the correct event.
        eventTag.setEvent(event);
        model.addAttribute("eventTag", eventTag); // Passes the DTO into the view.
        return "events/add-tag";
    }

    @PostMapping("add-tag")
    public String processAddTagForm(@ModelAttribute @Valid EventTagDTO eventTag, Errors errors, Model model) {
        // Using a DTO allows us to create and validate these objects through model binding. The same event and
        // tag relationship information could be processed without a DTO, but this would require passing query
        // parameters for the IDs of both event and tag objects, querying the eventRepository and tagRepository
        // for these items, validating those objects, etc. Simply put, the DTO makes this procedure cleaner and easier.
        if(!errors.hasErrors()) {
            Event event = eventTag.getEvent();  // Retrieves event from DTO
            Tag tag = eventTag.getTag();        // Retrieves tag from DTO
            if (!event.getTags().contains(tag)) {
                event.addTag(tag);              // If DTO doesn't already have the tag, adds it.
                eventRepository.save(event);    // Saves newly added tag to event.
            }
            return "redirect:detail?eventId=" + event.getId();
        }
        return "events/add-tag";
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
