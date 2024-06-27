package ru.yandex.practicum.filmorate.service.validate;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.film.FilmRepository;
import ru.yandex.practicum.filmorate.dao.review.ReviewRepository;
import ru.yandex.practicum.filmorate.dao.user.UserRepository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;

@Component
@RequiredArgsConstructor
public class Validate {
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final FilmRepository filmRepository;

    public void validateReview(Long id) {
        reviewRepository.getById(id).orElseThrow(() -> new NotFoundException("No review id = " + id));
    }

    public void validateUser(Long id) {
        userRepository.getById(id).orElseThrow(() -> new NotFoundException("No user id = " + id));
    }

    public void validateFilm(Long filmId) {
        filmRepository.getById(filmId).orElseThrow(() -> new NotFoundException("No film id = " + filmId));
    }
}
