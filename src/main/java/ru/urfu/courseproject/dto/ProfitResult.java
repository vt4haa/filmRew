package ru.urfu.courseproject.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

/**
 * Результат расчёта прибыли фильма.
 */
@Getter
@AllArgsConstructor
public class ProfitResult {

    private final String movieTitle;
    private final BigDecimal budget;
    private final BigDecimal boxOffice;
    private final BigDecimal marketing;
    private final BigDecimal profit;
    private final BigDecimal profitabilityPercent;
    private final String verdict;
}
