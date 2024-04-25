package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDate;

/**
 * Film.
 */
@Data
public class Film {

    private static final LocalDate MIN_DATE = LocalDate.of(1895, 12, 28);
    private int id;
    @NotBlank
    private String name;

    @Max(200)
    @NotBlank
    private String description;

    private LocalDate releaseDate;

    @Min(0)
    private int duration;

    @AssertTrue
    public boolean isReleaseDateValid() {
        return releaseDate.isAfter(MIN_DATE);
    }
}
