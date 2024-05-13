package ru.yandex.practicum.filmorate.service.film;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmRepository;
import ru.yandex.practicum.filmorate.storage.user.UserRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class GeneralFilmService implements FilmService {

    private final FilmRepository filmRepository;
    private final UserRepository userRepository;


    @Override
    public Film create(Film film) {
        return filmRepository.create(film);
    }

    @Override
    public Film update(Film film) {
        long filmId = film.getId();
        if (filmRepository.get(filmId).isEmpty()) {
            throw new NotFoundException("Film not found with id: " + filmId);
        }
        return filmRepository.update(film);
    }

    @Override
    public Film get(long filmId) {
        return getFilmFromRepository(filmId);
    }

    @Override
    public ArrayList<Film> getAll() {
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
        return filmRepository.get(filmId).orElseThrow(() -> new NotFoundException("Film not found with id: " + filmId));
    }

    private User getUserFromRepository(long userId) {
        return userRepository.get(userId).orElseThrow(() -> new NotFoundException("User not found with id: " + userId));
    }

    @Override
    public Collection<Film> getPopularFilms(int count) {
        List<Film> sortedFilms = filmRepository.getLikes().entrySet().stream()
                .sorted((entry1, entry2) -> Integer.compare(entry2.getValue().size(), entry1.getValue().size()))
                .limit(count)
                .map(Map.Entry::getKey)
                .map(this::getFilmFromRepository)
                .toList();

        return sortedFilms;
    }
}
