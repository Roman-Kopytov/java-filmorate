package ru.yandex.practicum.filmorate.service.review;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.film.FilmRepository;
import ru.yandex.practicum.filmorate.dao.review.ReviewRepository;
import ru.yandex.practicum.filmorate.dao.user.UserRepository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.service.validate.Validate;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GeneralReviewService implements ReviewService {
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final FilmRepository filmRepository;
    private final Validate validate;

    @Override
    public Review operationLike(Long reviewId, Long userId, int useful) {
        validate.validateReview(reviewId);
        validate.validateUser(userId);
        return reviewRepository.operationLike(reviewId, userId, useful);
    }

    @Override
    public Review deleteLike(Long reviewId, Long userId) {
        validate.validateReview(reviewId);
        validate.validateUser(userId);
        return reviewRepository.deleteLike(reviewId, userId);
    }


    @Override
    public Review create(Review review) {
        validate.validateFilm(review.getFilmId());
        validate.validateUser(review.getUserId());
        return reviewRepository.save(review);
    }

    @Override
    public Review update(Review review) {
        validate.validateReview(review.getReviewId());
        validate.validateFilm(review.getFilmId());
        validate.validateUser(review.getUserId());
        return reviewRepository.update(review);
    }

    @Override
    public Review delete(Long reviewId) {
        validate.validateReview(reviewId);
        return reviewRepository.delete(reviewId);
    }

    @Override
    public Review getById(Long id) {
        return reviewRepository.getById(id).orElseThrow(() -> new NotFoundException("No review id = " + id));
    }

    @Override
    public List<Review> getAll(int count, Long filmId) {
        if (filmId != null) {
            validate.validateFilm(filmId);
        }
        return reviewRepository.getAll(count, filmId);
    }
}
