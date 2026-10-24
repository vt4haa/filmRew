package ru.urfu.courseproject.controller;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import ru.urfu.courseproject.entity.User;
import ru.urfu.courseproject.service.CurrentUserService;

/**
 * Добавляет текущего пользователя во все модели представлений.
 */
@ControllerAdvice
public class GlobalModelAttributes {

    private final CurrentUserService currentUserService;

    public GlobalModelAttributes(CurrentUserService currentUserService) {
        this.currentUserService = currentUserService;
    }

    @ModelAttribute("currentUser")
    public User currentUser() {
        return currentUserService.getCurrentUser();
    }
}
