package org.launchcode.codingevents.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;


@Controller
@RequestMapping("events")
public class EventController {

    @GetMapping
    public String displayAllEvents(Model model) {
        List<String> eventList = new ArrayList<>();
        eventList.add("Code With Pride");
        eventList.add("Strange Loop");
        eventList.add("Apple WWDC");
        eventList.add("SpringOne Platform");
        model.addAttribute("events", eventList);
        return "events/index";
    }

    // Lives at /events/create
    @GetMapping("create")
    public String renderCreateEventForm() {
        return "events/create";
    }
}
