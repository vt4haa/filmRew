package ru.urfu.courseproject.service;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.urfu.courseproject.entity.Role;
import ru.urfu.courseproject.entity.User;
import ru.urfu.courseproject.repository.UserRepository;

/**
 * Вспомогательный сервис для получения текущего пользователя.
 */
@Service
public class CurrentUserService {

    private final UserRepository userRepository;

    public CurrentUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public String getUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()
                || authentication instanceof AnonymousAuthenticationToken) {
            return null;
        }
        return authentication.getName();
    }

    public User getCurrentUser() {
        String username = getUsername();
        if (username == null) {
            return null;
        }
        return userRepository.findByUsername(username).orElse(null);
    }

    public boolean isAdmin() {
        User user = getCurrentUser();
        return user != null && user.getRole() == Role.ADMIN;
    }
}
