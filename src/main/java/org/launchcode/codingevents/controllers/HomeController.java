package org.launchcode.codingevents.controllers;

import org.springframework.stereotype.Controller;

@Controller
public class HomeController {

    public String getIndex() {
        return "index";
    }
}
