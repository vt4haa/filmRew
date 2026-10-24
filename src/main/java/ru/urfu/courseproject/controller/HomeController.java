package ru.urfu.courseproject.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.urfu.courseproject.entity.User;
import ru.urfu.courseproject.service.ActorService;
import ru.urfu.courseproject.service.CurrentUserService;
import ru.urfu.courseproject.service.MovieService;

@Controller
public class HomeController {

    private final MovieService movieService;
    private final ActorService actorService;
    private final CurrentUserService currentUserService;

    public HomeController(MovieService movieService,
                          ActorService actorService,
                          CurrentUserService currentUserService) {
        this.movieService = movieService;
        this.actorService = actorService;
        this.currentUserService = currentUserService;
    }

    @GetMapping({"/", "/index"})
    public String index(Model model) {
        User user = currentUserService.getCurrentUser();
        model.addAttribute("moviesCount", movieService.countVisible());
        model.addAttribute("actorsCount", actorService.countVisible());
        model.addAttribute("movies", movieService.findAllVisible());
        model.addAttribute("userRole", user != null ? user.getRole() : null);
        return "index";
    }
}
