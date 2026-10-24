package ru.urfu.courseproject.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * DTO формы создания и редактирования актёра.
 */
@Getter
@Setter
@NoArgsConstructor
public class ActorDto {

    private Long id;

    @NotBlank(message = "Укажите имя актёра")
    @Size(max = 150, message = "Имя не должно превышать 150 символов")
    private String fullName;

    @NotBlank(message = "Укажите страну")
    @Size(max = 80, message = "Название страны не должно превышать 80 символов")
    private String country;

    @NotNull(message = "Укажите гонорар")
    @DecimalMin(value = "0.0", message = "Гонорар не может быть отрицательным")
    private BigDecimal fee;

    @NotNull(message = "Выберите фильм")
    private Long movieId;
}
