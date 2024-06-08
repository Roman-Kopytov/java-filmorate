package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;
import lombok.Data;
import ru.yandex.practicum.filmorate.model.Marker.Update;

import java.time.LocalDate;
import java.util.*;

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
    private Long duration;
    @NotNull
    private Set<Genre> genres=new LinkedHashSet<>();
@NotNull
    private Mpa mpa;

    @AssertTrue
    public boolean isReleaseDateValid() {
        if (releaseDate != null) {
            return releaseDate.isAfter(START_FILM_DATA);
        }
        return true;
    }
}
