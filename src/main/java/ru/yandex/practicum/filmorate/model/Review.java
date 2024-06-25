package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import ru.yandex.practicum.filmorate.model.Marker.Update;

@Data
@RequiredArgsConstructor
public class Review {
    @NotNull(groups = Update.class)
    Long reviewId;
    @NotNull
    String content;
    @NotNull
    Boolean isPositive;
    @NotNull
    Long userId;
    @NotNull
    Long filmId;
    int useful = 0;

    public Review(String content, boolean b, long userId, long filmId) {
        this.content = content;
        this.isPositive = b;
        this.userId = userId;
        this.filmId = filmId;
    }
}
