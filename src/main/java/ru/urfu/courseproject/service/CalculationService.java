package ru.urfu.courseproject.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.urfu.courseproject.dto.ProfitResult;
import ru.urfu.courseproject.entity.Movie;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Сервис расчёта прибыли и рентабельности фильма.
 */
@Service
public class CalculationService {

    private final MovieService movieService;

    public CalculationService(MovieService movieService) {
        this.movieService = movieService;
    }

    @Transactional(readOnly = true)
    public ProfitResult calculate(Long movieId, BigDecimal marketing) {
        Movie movie = movieService.findVisibleById(movieId);
        BigDecimal safeMarketing = marketing == null ? BigDecimal.ZERO : marketing;
        BigDecimal totalCosts = movie.getBudget().add(safeMarketing);
        BigDecimal profit = movie.getBoxOffice().subtract(totalCosts);

        BigDecimal profitability = BigDecimal.ZERO;
        if (totalCosts.compareTo(BigDecimal.ZERO) > 0) {
            profitability = profit.multiply(BigDecimal.valueOf(100))
                    .divide(totalCosts, 2, RoundingMode.HALF_UP);
        }

        String verdict;
        if (profit.compareTo(BigDecimal.ZERO) > 0) {
            verdict = "Фильм прибыльный";
        } else if (profit.compareTo(BigDecimal.ZERO) == 0) {
            verdict = "Фильм вышел в ноль";
        } else {
            verdict = "Фильм убыточный";
        }

        ProfitResult result = new ProfitResult(movie.getTitle(), movie.getBudget(), movie.getBoxOffice(),
                safeMarketing, profit, profitability, verdict);
        return result;
    }
}
