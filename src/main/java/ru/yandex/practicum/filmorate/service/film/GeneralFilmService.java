package ru.yandex.practicum.filmorate.service.film;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.dao.film.FilmRepository;
import ru.yandex.practicum.filmorate.dao.user.UserRepository;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class GeneralFilmService implements FilmService {

    private final FilmRepository filmRepository;
    private final UserRepository userRepository;


    @Override
    public Film create(Film film) {
        return filmRepository.save(film);
    }

    @Override
    public Film update(Film film) {
        long filmId = film.getId();
        if (filmRepository.getById(filmId).isEmpty()) {
            throw new NotFoundException("Film not found with id: " + filmId);
        }
        return filmRepository.update(film);
    }

    @Override
    public Film get(long filmId) {
        return getFilmFromRepository(filmId);
    }

    @Override
    public List<Film> getAll() {
        return filmRepository.getAll();
    }

    @Override
    public void addLike(long userId, long filmId) {
        filmRepository.addLike(getFilmFromRepository(filmId), getUserFromRepository(userId));
    }

    @Override
    public void deleteLike(long userId, long filmId) {
        filmRepository.deleteLike(getFilmFromRepository(filmId), getUserFromRepository(userId));
    }

    private Film getFilmFromRepository(long filmId) {
        return filmRepository.getById(filmId).orElseThrow(() -> new NotFoundException("Film not found with id: " + filmId));
    }

    private User getUserFromRepository(long userId) {
        return userRepository.getById(userId).orElseThrow(() -> new NotFoundException("User not found with id: " + userId));
    }

    @Override
    public List<Film> getPopularFilms(int count) {
        List<Film> sortedFilms = filmRepository.getLikes().entrySet().stream()
                .sorted((entry1, entry2) -> Integer.compare(entry2.getValue().size(), entry1.getValue().size()))
                .limit(count)
                .map(Map.Entry::getKey)
                .map(this::getFilmFromRepository)
                .toList();

        return sortedFilms;
    }
}
