package ru.yandex.practicum.filmorate.service.review;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.film.FilmRepository;
import ru.yandex.practicum.filmorate.dao.review.ReviewRepository;
import ru.yandex.practicum.filmorate.dao.user.UserRepository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Review;

import java.util.Comparator;
import java.util.LinkedList;

@Service
@RequiredArgsConstructor
public class GeneralReviewService implements ReviewService {
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final FilmRepository filmRepository;

    @Override
    public Review operationLike(Long reviewId, Long userId, int useful) {
        validateReview(reviewId);
        validateUser(userId);
        return reviewRepository.operationLike(reviewId, userId, useful);
    }

    @Override
    public Review deleteLike(Long reviewId, Long userId) {
        validateReview(reviewId);
        validateUser(userId);
        return reviewRepository.deleteLike(reviewId, userId);
    }


    @Override
    public Review create(Review review) {
        validateFilm(review.getFilmId());
        validateUser(review.getUserId());
        return reviewRepository.save(review);
    }

    @Override
    public Review update(Review review) {
        validateFilm(review.getFilmId());
        validateUser(review.getUserId());
        return reviewRepository.update(review);
    }

    @Override
    public Review delete(Long reviewId) {
        validateReview(reviewId);
        return reviewRepository.delete(reviewId);
    }

    @Override
    public Review getById(Long id) {
        return reviewRepository.getById(id).orElseThrow(() -> new NotFoundException("No review id = " + id));
    }

    @Override
    public LinkedList<Review> getAll(int count, Long filmId) {
        if (filmId != null) {
            validateFilm(filmId);
        }
        LinkedList<Review> reviews = new LinkedList<>(reviewRepository.getAll(count, filmId));
        reviews.sort(Comparator.comparing(Review::getUseful).reversed());
        return reviews;
    }

    private void validateReview(Long id) {
        reviewRepository.getById(id).orElseThrow(() -> new NotFoundException("No review id = " + id));
    }

    private void validateUser(Long id) {
        userRepository.getById(id).orElseThrow(() -> new NotFoundException("No user id = " + id));
    }

    private void validateFilm(Long filmId) {
        filmRepository.getById(filmId).orElseThrow(() -> new NotFoundException("No film id = " + filmId));
    }

}
