package ru.yandex.practicum.filmorate.service.review;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Review;

import java.util.List;

@Service
public interface ReviewService {

    Review create(Review review);

    Review update(Review review);

    Review delete(Long userId);

    Review getById(Long id);

    List<Review> getAll(int count, Long filmId);

    Review operationLike(Long id, Long userId, boolean b);

    Review deleteLike(Long id, Long userId, boolean b);

}
