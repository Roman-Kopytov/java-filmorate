package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.model.Marker.Update;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;

/**
 * Film.
 */
@Data
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@AllArgsConstructor
public class Film {

    private static final LocalDate START_FILM_DATA = LocalDate.of(1895, 12, 28);

    @NotNull(groups = Update.class)
    private Long id;

    @NotBlank
    private String name;

    @NotBlank
    @Size(max = 200)
    private String description;

    private LocalDate releaseDate;

    @Positive
    @NotNull
    private Long duration;

    private LinkedHashSet<Genre> genres;

    @NotNull
    private Mpa mpa;

    private HashSet<Director> directors;

    @AssertTrue
    public boolean isReleaseDateValid() {
        if (releaseDate != null) {
            return releaseDate.isAfter(START_FILM_DATA);
        }
        return true;
    }
}
