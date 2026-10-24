package ru.urfu.courseproject.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Таблица характеристик основной сущности (дополнительные сведения о фильме).
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "movie_details")
public class MovieDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "duration_minutes")
    private Integer durationMinutes;

    private Double rating;

    @Column(length = 100)
    private String studio;

    @Column(length = 1000)
    private String synopsis;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "movie_id", nullable = false, unique = true)
    private Movie movie;
}
