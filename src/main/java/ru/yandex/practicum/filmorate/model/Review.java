package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class Review {
    Long id;
    String content;
    @NotNull
    Boolean isPositive;
    @NotNull
    @Positive
    Long userId;
    @NotNull
    @Positive
    Long filmId;
    int useful;

    public Review(String content, boolean b, long userId, long filmId) {
        this.content = content;
        this.isPositive = b;
        this.userId = userId;
        this.filmId = filmId;
    }
}
