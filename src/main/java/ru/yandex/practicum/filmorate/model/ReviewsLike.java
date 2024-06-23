package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ReviewsLike {
    private final Long id;
    private final Long userId;
    private final int useful;
}
