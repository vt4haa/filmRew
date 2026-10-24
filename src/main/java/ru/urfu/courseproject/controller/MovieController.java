package ru.urfu.courseproject.controller;

import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.urfu.courseproject.dto.MovieDto;
import ru.urfu.courseproject.entity.Movie;
import ru.urfu.courseproject.service.ActorService;
import ru.urfu.courseproject.service.MovieService;

@Controller
@RequestMapping("/movies")
public class MovieController {

    private final MovieService movieService;
    private final ActorService actorService;

    public MovieController(MovieService movieService, ActorService actorService) {
        this.movieService = movieService;
        this.actorService = actorService;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("movies", movieService.findAllVisible());
        return "movies/list";
    }

    @GetMapping("/{id}")
    public String view(@PathVariable Long id, Model model) {
        model.addAttribute("movie", movieService.findVisibleById(id));
        model.addAttribute("actors", actorService.findByMovieId(id));
        return "movies/view";
    }

    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("movie", new MovieDto());
        return "movies/form";
    }

    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        Movie movie = movieService.findVisibleById(id);
        model.addAttribute("movie", movieService.toDto(movie));
        return "movies/form";
    }

    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @PostMapping("/save")
    public String save(@Valid @ModelAttribute("movie") MovieDto dto,
                       BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "movies/form";
        }
        movieService.save(dto);
        return "redirect:/movies";
    }

    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        movieService.delete(id);
        return "redirect:/movies";
    }
}
