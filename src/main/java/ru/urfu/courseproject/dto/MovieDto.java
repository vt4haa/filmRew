package ru.urfu.courseproject.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * DTO формы создания и редактирования фильма.
 */
@Getter
@Setter
@NoArgsConstructor
public class MovieDto {

    private Long id;

    @NotBlank(message = "Название фильма обязательно")
    @Size(max = 150, message = "Название не должно превышать 150 символов")
    private String title;

    @NotBlank(message = "Укажите жанр")
    @Size(max = 60, message = "Жанр не должен превышать 60 символов")
    private String genre;

    @NotNull(message = "Укажите год выпуска")
    @Min(value = 1888, message = "Год выпуска не может быть раньше 1888")
    @Max(value = 2100, message = "Год выпуска не может быть позже 2100")
    private Integer releaseYear;

    @NotNull(message = "Укажите бюджет")
    @DecimalMin(value = "0.0", message = "Бюджет не может быть отрицательным")
    private BigDecimal budget;

    @NotNull(message = "Укажите кассовые сборы")
    @DecimalMin(value = "0.0", message = "Сборы не могут быть отрицательными")
    private BigDecimal boxOffice;

    @Min(value = 1, message = "Длительность должна быть положительной")
    @Max(value = 600, message = "Длительность не может превышать 600 минут")
    private Integer durationMinutes;

    @DecimalMin(value = "0.0", message = "Рейтинг не может быть меньше 0")
    @DecimalMax(value = "10.0", message = "Рейтинг не может быть больше 10")
    private Double rating;

    @Size(max = 100, message = "Название студии не должно превышать 100 символов")
    private String studio;

    @Size(max = 1000, message = "Описание не должно превышать 1000 символов")
    private String synopsis;
}
