package ru.urfu.courseproject.controller;

import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import ru.urfu.courseproject.dto.RegistrationDto;
import ru.urfu.courseproject.service.UserService;

@Controller
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String registerForm(Model model) {
        model.addAttribute("user", new RegistrationDto());
        return "register";
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("user") RegistrationDto dto,
                           BindingResult bindingResult,
                           Model model) {
        if (!dto.getPassword().equals(dto.getConfirmPassword())) {
            bindingResult.rejectValue("confirmPassword", "password.mismatch", "Пароли не совпадают");
        }
        if (userService.findByUsername(dto.getUsername()) != null) {
            bindingResult.rejectValue("username", "username.exists",
                    "Пользователь с таким логином уже существует");
        }
        if (bindingResult.hasErrors()) {
            return "register";
        }

        userService.register(dto);
        model.addAttribute("registeredUsername", dto.getUsername());
        return "redirect:/login?registered";
    }
}
