package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class Review {
    @NotNull(groups = Marker.Update.class)
    Long reviewId;
    String content;
    Boolean isPositive;
    @NotNull
    Long userId;
    @NotNull
    Long filmId;
    int useful = 0;

    public Review(long id, String content, boolean b, long userId, long filmId, int useful) {
        this(content, b, userId, filmId);
        this.reviewId = id;
        this.useful = useful;
    }

    public Review(String content, boolean b, long userId, long filmId) {
        this.content = content;
        this.isPositive = b;
        this.userId = userId;
        this.filmId = filmId;
    }
}
