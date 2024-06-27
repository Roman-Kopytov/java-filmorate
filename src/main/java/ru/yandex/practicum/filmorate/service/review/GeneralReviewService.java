package ru.yandex.practicum.filmorate.service.review;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.event.JdbcEventRepository;
import ru.yandex.practicum.filmorate.dao.film.FilmRepository;
import ru.yandex.practicum.filmorate.dao.review.ReviewRepository;
import ru.yandex.practicum.filmorate.dao.user.UserRepository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.EventType;
import ru.yandex.practicum.filmorate.model.Operation;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.service.validate.Validate;

import java.util.Comparator;
import java.util.LinkedList;

@Service
@RequiredArgsConstructor
public class GeneralReviewService implements ReviewService {
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final FilmRepository filmRepository;
    private final JdbcEventRepository eventRepository;
    private final Validate validate;

    @Override
    public Review operationLike(Long reviewId, Long userId, boolean like) {
        validate.validateReview(reviewId);
        validate.validateUser(userId);
        return reviewRepository.operationLike(reviewId, userId, like ? 1 : -1);
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
        Review savedReview = reviewRepository.save(review);
        eventRepository.saveEvent(review.getUserId(), review.getReviewId(), EventType.REVIEW, Operation.ADD);
        return savedReview;
    }

    @Override
    public Review update(Review review) {
        validate.validateFilm(review.getFilmId());
        validate.validateUser(review.getUserId());
        Review newReview = reviewRepository.update(review);
        eventRepository.saveEvent(newReview.getUserId(), newReview.getReviewId(), EventType.REVIEW, Operation.UPDATE);
        return newReview;
    }

    @Override
    public Review delete(Long reviewId) {
        validate.validateReview(reviewId);
        Review oldReview = reviewRepository.getById(reviewId).get();
        Review deleted = reviewRepository.delete(reviewId);
        eventRepository.saveEvent(oldReview.getUserId(), reviewId, EventType.REVIEW, Operation.REMOVE);
        return deleted;
    }

    @Override
    public Review getById(Long id) {
        return reviewRepository.getById(id).orElseThrow(() -> new NotFoundException("No review id = " + id));
    }

    @Override
    public LinkedList<Review> getAll(int count, Long filmId) {
        if (filmId != null) {
            validate.validateFilm(filmId);
        }
        LinkedList<Review> reviews = new LinkedList<>(reviewRepository.getAll(count, filmId));
        reviews.sort(Comparator.comparing(Review::getUseful).reversed());
        return reviews;
    }



}
