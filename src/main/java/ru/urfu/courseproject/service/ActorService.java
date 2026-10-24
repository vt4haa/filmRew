package ru.urfu.courseproject.service;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import ru.urfu.courseproject.dto.ActorDto;
import ru.urfu.courseproject.entity.Actor;
import ru.urfu.courseproject.entity.Movie;
import ru.urfu.courseproject.repository.ActorRepository;

import java.util.List;

/**
 * Бизнес-логика работы с актёрами.
 */
@Service
public class ActorService {

    private final ActorRepository actorRepository;
    private final MovieService movieService;
    private final CurrentUserService currentUserService;

    public ActorService(ActorRepository actorRepository,
                        MovieService movieService,
                        CurrentUserService currentUserService) {
        this.actorRepository = actorRepository;
        this.movieService = movieService;
        this.currentUserService = currentUserService;
    }

    @Transactional(readOnly = true)
    public List<Actor> findAllVisible() {
        if (currentUserService.isAdmin()) {
            return actorRepository.findAllByOrderByFullNameAsc();
        }
        return actorRepository.findAllByOwnerUsernameOrderByFullNameAsc(currentUserService.getUsername());
    }

    @Transactional(readOnly = true)
    public Actor findVisibleById(Long id) {
        if (currentUserService.isAdmin()) {
            return actorRepository.findById(id)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Актёр не найден"));
        }
        return actorRepository.findByIdAndOwnerUsername(id, currentUserService.getUsername())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Актёр не найден"));
    }

    @Transactional(readOnly = true)
    public List<Actor> findByMovieId(Long movieId) {
        movieService.findVisibleById(movieId);
        return actorRepository.findAllByMovieId(movieId);
    }

    @Transactional(readOnly = true)
    public ActorDto toDto(Actor actor) {
        ActorDto dto = new ActorDto();
        dto.setId(actor.getId());
        dto.setFullName(actor.getFullName());
        dto.setCountry(actor.getCountry());
        dto.setFee(actor.getFee());
        dto.setMovieId(actor.getMovie().getId());
        return dto;
    }

    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @Transactional
    public Actor save(ActorDto dto) {
        Movie movie = movieService.findVisibleById(dto.getMovieId());

        Actor actor;
        boolean isNew = dto.getId() == null;
        if (isNew) {
            actor = new Actor();
        } else {
            actor = findVisibleById(dto.getId());
        }

        actor.setFullName(dto.getFullName().trim());
        actor.setCountry(dto.getCountry().trim());
        actor.setFee(dto.getFee());
        actor.setMovie(movie);
        actor.setOwner(movie.getOwner());

        Actor saved = actorRepository.save(actor);
        return saved;
    }

    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @Transactional
    public void delete(Long id) {
        Actor actor = findVisibleById(id);
        actorRepository.delete(actor);
    }

    @Transactional(readOnly = true)
    public long countVisible() {
        return findAllVisible().size();
    }

    @Transactional(readOnly = true)
    public long countAll() {
        return actorRepository.count();
    }
}
