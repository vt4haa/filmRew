package ru.urfu.courseproject.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * Основная сущность варианта — фильм.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "movies")
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String title;

    @Column(nullable = false, length = 60)
    private String genre;

    @Column(name = "release_year", nullable = false)
    private Integer releaseYear;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal budget;

    @Column(name = "box_office", nullable = false, precision = 15, scale = 2)
    private BigDecimal boxOffice;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    @OneToOne(mappedBy = "movie", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private MovieDetail details;

    public void setDetails(MovieDetail details) {
        if (details == null) {
            if (this.details != null) {
                this.details.setMovie(null);
            }
        } else {
            details.setMovie(this);
        }
        this.details = details;
    }
}
