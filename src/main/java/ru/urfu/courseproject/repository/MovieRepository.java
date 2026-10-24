package ru.urfu.courseproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.urfu.courseproject.entity.Movie;

import java.util.List;
import java.util.Optional;

public interface MovieRepository extends JpaRepository<Movie, Long> {

    List<Movie> findAllByOrderByTitleAsc();

    List<Movie> findAllByOwnerUsernameOrderByTitleAsc(String username);

    Optional<Movie> findByIdAndOwnerUsername(Long id, String username);

    List<Movie> findAllByOwnerId(Long ownerId);
}
