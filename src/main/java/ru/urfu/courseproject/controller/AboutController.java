package ru.urfu.courseproject.controller;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
public class AboutController {

    @GetMapping("/about")
    public String about() {
        return "about";
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @GetMapping("/access-denied")
    public String accessDenied() {
        return "error/403";
    }
}
