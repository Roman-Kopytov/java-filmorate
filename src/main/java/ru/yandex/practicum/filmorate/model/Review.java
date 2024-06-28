package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.model.Marker.Update;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Review {
    @NotNull(groups = Update.class)
    Long reviewId;
    @NotBlank
    String content;
    @NotNull
    Boolean isPositive;
    @NotNull
    Long userId;
    @NotNull
    Long filmId;
    int useful;

    public Review(String content, boolean b, long userId, long filmId) {
        this.content = content;
        this.isPositive = b;
        this.userId = userId;
        this.filmId = filmId;
        this.useful = 0;
    }
}
