package ru.urfu.courseproject.service;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import ru.urfu.courseproject.dto.MovieDto;
import ru.urfu.courseproject.entity.Movie;
import ru.urfu.courseproject.entity.MovieDetail;
import ru.urfu.courseproject.entity.User;
import ru.urfu.courseproject.repository.ActorRepository;
import ru.urfu.courseproject.repository.MovieRepository;

import java.util.List;

/**
 * Бизнес-логика работы с фильмами с учётом ролей.
 * ADMIN видит все фильмы, USER — только созданные им записи,
 * READ_ONLY не может изменять данные.
 */
@Service
public class MovieService {

    private final MovieRepository movieRepository;
    private final ActorRepository actorRepository;
    private final CurrentUserService currentUserService;

    public MovieService(MovieRepository movieRepository,
                        ActorRepository actorRepository,
                        CurrentUserService currentUserService) {
        this.movieRepository = movieRepository;
        this.actorRepository = actorRepository;
        this.currentUserService = currentUserService;
    }

    @Transactional(readOnly = true)
    public List<Movie> findAllVisible() {
        if (currentUserService.isAdmin()) {
            return movieRepository.findAllByOrderByTitleAsc();
        }
        String username = currentUserService.getUsername();
        return movieRepository.findAllByOwnerUsernameOrderByTitleAsc(username);
    }

    @Transactional(readOnly = true)
    public Movie findVisibleById(Long id) {
        if (currentUserService.isAdmin()) {
            return movieRepository.findById(id)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Фильм не найден"));
        }
        String username = currentUserService.getUsername();
        return movieRepository.findByIdAndOwnerUsername(id, username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Фильм не найден"));
    }

    @Transactional(readOnly = true)
    public MovieDto toDto(Movie movie) {
        MovieDto dto = new MovieDto();
        dto.setId(movie.getId());
        dto.setTitle(movie.getTitle());
        dto.setGenre(movie.getGenre());
        dto.setReleaseYear(movie.getReleaseYear());
        dto.setBudget(movie.getBudget());
        dto.setBoxOffice(movie.getBoxOffice());
        if (movie.getDetails() != null) {
            dto.setDurationMinutes(movie.getDetails().getDurationMinutes());
            dto.setRating(movie.getDetails().getRating());
            dto.setStudio(movie.getDetails().getStudio());
            dto.setSynopsis(movie.getDetails().getSynopsis());
        }
        return dto;
    }

    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @Transactional
    public Movie save(MovieDto dto) {
        User currentUser = currentUserService.getCurrentUser();
        if (currentUser == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

        Movie movie;
        boolean isNew = dto.getId() == null;
        if (isNew) {
            movie = new Movie();
            movie.setOwner(currentUser);
        } else {
            movie = findVisibleById(dto.getId());
        }

        movie.setTitle(dto.getTitle().trim());
        movie.setGenre(dto.getGenre().trim());
        movie.setReleaseYear(dto.getReleaseYear());
        movie.setBudget(dto.getBudget());
        movie.setBoxOffice(dto.getBoxOffice());

        MovieDetail detail = movie.getDetails();
        if (detail == null) {
            detail = new MovieDetail();
        }
        detail.setDurationMinutes(dto.getDurationMinutes());
        detail.setRating(dto.getRating());
        detail.setStudio(dto.getStudio());
        detail.setSynopsis(dto.getSynopsis());
        movie.setDetails(detail);

        Movie saved = movieRepository.save(movie);
        return saved;
    }

    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @Transactional
    public void delete(Long id) {
        Movie movie = findVisibleById(id);
        actorRepository.deleteAllByMovieId(movie.getId());
        movieRepository.delete(movie);
    }

    @Transactional(readOnly = true)
    public long countVisible() {
        return findAllVisible().size();
    }

    @Transactional(readOnly = true)
    public long countAll() {
        return movieRepository.count();
    }
}
