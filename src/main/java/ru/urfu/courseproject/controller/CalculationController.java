package ru.urfu.courseproject.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.urfu.courseproject.service.CalculationService;
import ru.urfu.courseproject.service.MovieService;

import java.math.BigDecimal;

@Controller
public class CalculationController {

    private final CalculationService calculationService;
    private final MovieService movieService;

    public CalculationController(CalculationService calculationService, MovieService movieService) {
        this.calculationService = calculationService;
        this.movieService = movieService;
    }

    @GetMapping("/calculator")
    public String calculator(Model model) {
        model.addAttribute("movies", movieService.findAllVisible());
        return "calculator";
    }

    @PostMapping("/calculator")
    public String calculate(@RequestParam(required = false) Long movieId,
                            @RequestParam(required = false) BigDecimal marketing,
                            Model model) {
        model.addAttribute("movies", movieService.findAllVisible());
        model.addAttribute("selectedMovieId", movieId);
        model.addAttribute("marketing", marketing);

        if (movieId == null) {
            model.addAttribute("error", "Выберите фильм для расчёта");
            return "calculator";
        }
        if (marketing != null && marketing.compareTo(BigDecimal.ZERO) < 0) {
            model.addAttribute("error", "Затраты на маркетинг не могут быть отрицательными");
            return "calculator";
        }

        model.addAttribute("result", calculationService.calculate(movieId, marketing));
        return "calculator";
    }
}
