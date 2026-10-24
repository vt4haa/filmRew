package ru.urfu.courseproject.controller;

import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.urfu.courseproject.dto.ActorDto;
import ru.urfu.courseproject.entity.Actor;
import ru.urfu.courseproject.service.ActorService;
import ru.urfu.courseproject.service.MovieService;

@Controller
@RequestMapping("/actors")
public class ActorController {

    private final ActorService actorService;
    private final MovieService movieService;

    public ActorController(ActorService actorService, MovieService movieService) {
        this.actorService = actorService;
        this.movieService = movieService;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("actors", actorService.findAllVisible());
        return "actors/list";
    }

    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("actor", new ActorDto());
        model.addAttribute("movies", movieService.findAllVisible());
        return "actors/form";
    }

    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        Actor actor = actorService.findVisibleById(id);
        model.addAttribute("actor", actorService.toDto(actor));
        model.addAttribute("movies", movieService.findAllVisible());
        return "actors/form";
    }

    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @PostMapping("/save")
    public String save(@Valid @ModelAttribute("actor") ActorDto dto,
                       BindingResult bindingResult,
                       Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("movies", movieService.findAllVisible());
            return "actors/form";
        }
        actorService.save(dto);
        return "redirect:/actors";
    }

    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        actorService.delete(id);
        return "redirect:/actors";
    }
}
