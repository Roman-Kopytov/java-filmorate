package ru.yandex.practicum.filmorate.dao.review;

import ru.yandex.practicum.filmorate.model.Review;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository {

    Review save(Review review);

    Optional<Review> getById(Long reviewId);

    Review update(Review review);

    Review delete(long reviewId);

    List<Review> getAll(int count, Long filmId);

    Review operationLike(Long id, Long userId, Integer useful);

    Review deleteLike(Long id, Long userId, Integer useful);
}
