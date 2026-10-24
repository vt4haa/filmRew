package ru.urfu.courseproject.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import ru.urfu.courseproject.entity.Role;

/**
 * DTO формы изменения роли пользователя (доступна администратору).
 */
@Getter
@Setter
public class RoleChangeDto {

    @NotNull
    private Long userId;

    @NotNull(message = "Выберите роль")
    private Role role;
}
