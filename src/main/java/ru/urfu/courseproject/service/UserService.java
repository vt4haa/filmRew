package ru.urfu.courseproject.service;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.urfu.courseproject.dto.RegistrationDto;
import ru.urfu.courseproject.entity.Role;
import ru.urfu.courseproject.entity.User;
import ru.urfu.courseproject.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Работа с пользователями: регистрация, просмотр, изменение ролей.
 */
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public User register(RegistrationDto dto) {
        if (!dto.getPassword().equals(dto.getConfirmPassword())) {
            throw new IllegalArgumentException("Пароли не совпадают");
        }
        if (userRepository.existsByUsername(dto.getUsername())) {
            throw new IllegalArgumentException("Пользователь с таким логином уже существует");
        }

        User user = new User();
        user.setUsername(dto.getUsername().trim());
        user.setFullName(dto.getFullName().trim());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setRole(Role.READ_ONLY);
        user.setCreatedAt(LocalDateTime.now());
        User saved = userRepository.save(user);
        return saved;
    }

    @Transactional(readOnly = true)
    public User findByUsername(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }

    @Transactional(readOnly = true)
    public List<User> findAll() {
        return userRepository.findAllByOrderByUsernameAsc();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public void changeRole(Long userId, Role role) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Пользователь не найден"));
        user.setRole(role);
        userRepository.save(user);
    }
}
