package ru.urfu.courseproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.urfu.courseproject.entity.Actor;

import java.util.List;
import java.util.Optional;

public interface ActorRepository extends JpaRepository<Actor, Long> {

    List<Actor> findAllByOrderByFullNameAsc();

    List<Actor> findAllByOwnerUsernameOrderByFullNameAsc(String username);

    Optional<Actor> findByIdAndOwnerUsername(Long id, String username);

    List<Actor> findAllByMovieId(Long movieId);

    void deleteAllByMovieId(Long movieId);
}
