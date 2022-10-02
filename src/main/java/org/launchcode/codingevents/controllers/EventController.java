package org.launchcode.codingevents.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;


@Controller
@RequestMapping("events")
public class EventController {
    private static List<String> eventList = new ArrayList<>();

    @GetMapping
    public String displayAllEvents(Model model) {
        model.addAttribute("events", eventList);
        return "events/index";
    }

    // Lives at /events/create
    @GetMapping("create")
    public String renderCreateEventForm() {
        return "events/create";
    }

    // Lives at /events/create   ***This is ok because ^ is a Get request while v is a Post request***
    @PostMapping("create")
    public String createEvent(@RequestParam String eventName) {
        eventList.add(eventName);
        return "redirect:";
    }
}
