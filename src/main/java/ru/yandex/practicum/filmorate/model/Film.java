package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;
import lombok.Data;
import ru.yandex.practicum.filmorate.model.Marker.Update;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Film.
 */
@Data
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
    private int duration;
    @NotNull
    private Set<Integer> genreIds;
    @NotNull
    private int mpa;

    @AssertTrue
    public boolean isReleaseDateValid() {
        if (releaseDate != null) {
            return releaseDate.isAfter(START_FILM_DATA);
        }
        return true;
    }

    public Integer getRating() {
        return  mpa;
    }

    public List<Integer> getGenreId() {
        return new ArrayList<>(genreIds);
    }
}
