package ru.urfu.courseproject.config;

import java.math.BigDecimal;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import ru.urfu.courseproject.entity.Actor;
import ru.urfu.courseproject.entity.Movie;
import ru.urfu.courseproject.entity.MovieDetail;
import ru.urfu.courseproject.entity.Role;
import ru.urfu.courseproject.entity.User;
import ru.urfu.courseproject.repository.ActorRepository;
import ru.urfu.courseproject.repository.MovieRepository;
import ru.urfu.courseproject.repository.UserRepository;

/**
 * Заполняет базу демонстрационными данными при первом запуске.
 */
@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final MovieRepository movieRepository;
    private final ActorRepository actorRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserRepository userRepository,
                           MovieRepository movieRepository,
                           ActorRepository actorRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.movieRepository = movieRepository;
        this.actorRepository = actorRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        if (userRepository.count() > 0) {
            return;
        }

        User admin = createUser("admin", "Администратор Системы", "admin123", Role.ADMIN);
        User user = createUser("user", "Иван Пользователь", "user123", Role.USER);
        createUser("guest", "Гость Только-Чтение", "guest123", Role.READ_ONLY);

        Movie interstellar = createMovie("Интерстеллар", "Фантастика", 2014,
                "165000000", "701800000", admin,
                169, 8.7, "Legendary Pictures",
                "Команда исследователей отправляется сквозь червоточину, чтобы найти новый дом для человечества.");

        Movie inception = createMovie("Начало", "Триллер", 2010,
                "160000000", "836800000", user,
                148, 8.8, "Warner Bros.",
                "Профессиональный вор проникает в сны людей, чтобы украсть или внедрить идею.");

        Movie matrix = createMovie("Матрица", "Фантастика", 1999,
                "63000000", "467200000", user,
                136, 8.7, "Warner Bros.",
                "Хакер узнаёт, что реальность — это компьютерная симуляция.");

        createActor("Киану Ривз", "США", "15000000", matrix, user);
        createActor("Лоуренс Фишборн", "США", "4000000", matrix, user);
        createActor("Леонардо ДиКаприо", "США", "20000000", inception, user);
        createActor("Джозеф Гордон-Левитт", "США", "9000000", inception, user);
        createActor("Мэттью МакКонахи", "США", "18000000", interstellar, admin);
        createActor("Энн Хэтэуэй", "США", "12000000", interstellar, admin);
    }

    private User createUser(String username, String fullName, String password, Role role) {
        User user = new User();
        user.setUsername(username);
        user.setFullName(fullName);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(role);
        user.setCreatedAt(java.time.LocalDateTime.now());
        return userRepository.save(user);
    }

    private Movie createMovie(String title, String genre, int year, String budget, String boxOffice,
                              User owner, Integer duration, Double rating, String studio, String synopsis) {
        Movie movie = new Movie();
        movie.setTitle(title);
        movie.setGenre(genre);
        movie.setReleaseYear(year);
        movie.setBudget(new BigDecimal(budget));
        movie.setBoxOffice(new BigDecimal(boxOffice));
        movie.setOwner(owner);

        MovieDetail detail = new MovieDetail();
        detail.setDurationMinutes(duration);
        detail.setRating(rating);
        detail.setStudio(studio);
        detail.setSynopsis(synopsis);
        movie.setDetails(detail);

        return movieRepository.save(movie);
    }

    private void createActor(String fullName, String country, String fee, Movie movie, User owner) {
        Actor actor = new Actor();
        actor.setFullName(fullName);
        actor.setCountry(country);
        actor.setFee(new BigDecimal(fee));
        actor.setMovie(movie);
        actor.setOwner(owner);
        actorRepository.save(actor);
    }


}
