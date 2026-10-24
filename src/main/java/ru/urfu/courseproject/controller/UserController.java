package ru.urfu.courseproject.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.urfu.courseproject.entity.Role;
import ru.urfu.courseproject.entity.User;
import ru.urfu.courseproject.service.CurrentUserService;
import ru.urfu.courseproject.service.UserService;

@Controller
public class UserController {

    private final UserService userService;
    private final CurrentUserService currentUserService;

    public UserController(UserService userService, CurrentUserService currentUserService) {
        this.userService = userService;
        this.currentUserService = currentUserService;
    }

    @GetMapping("/users")
    public String users(Model model) {
        model.addAttribute("users", userService.findAll());
        model.addAttribute("roles", Role.values());
        return "users";
    }

    @PostMapping("/users/{id}/role")
    public String changeRole(@PathVariable Long id, @RequestParam Role role) {
        User currentUser = currentUserService.getCurrentUser();
        if (currentUser != null && currentUser.getId().equals(id)) {
            return "redirect:/users?self";
        }
        userService.changeRole(id, role);
        return "redirect:/users";
    }
}
