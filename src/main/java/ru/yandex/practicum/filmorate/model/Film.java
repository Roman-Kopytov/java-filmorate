package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;

/**
 * Film.
 */
@Data
public class Film {

    private static final LocalDate START_FILM_DATA = LocalDate.of(1895, 12, 28);

    private int id;
    @NotBlank
    private String name;

    @NotBlank
    @Size(max = 200)
    private String description;

    private LocalDate releaseDate;

    @Positive
    @NotNull
    private int duration;

    @AssertTrue
    public boolean isReleaseDateValid() {
        if (releaseDate != null) {
            return releaseDate.isAfter(START_FILM_DATA);
        } else return true;
    }
}
