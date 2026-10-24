package ru.urfu.courseproject.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO формы регистрации.
 */
@Getter
@Setter
@NoArgsConstructor
public class RegistrationDto {

    @NotBlank(message = "Укажите логин")
    @Size(min = 3, max = 50, message = "Логин должен содержать от 3 до 50 символов")
    private String username;

    @NotBlank(message = "Укажите имя и фамилию")
    @Size(max = 100, message = "Имя не должно превышать 100 символов")
    private String fullName;

    @NotBlank(message = "Укажите пароль")
    @Size(min = 6, max = 100, message = "Пароль должен содержать от 6 до 100 символов")
    private String password;

    @NotBlank(message = "Повторите пароль")
    private String confirmPassword;
}
