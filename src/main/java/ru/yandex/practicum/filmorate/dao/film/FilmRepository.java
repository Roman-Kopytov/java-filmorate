package ru.yandex.practicum.filmorate.dao.film;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;


public interface FilmRepository {

    Optional<Film> getById(long filmId);

    void deleteFilmById(long id);

    Film save(Film film);

    Film update(Film film);

    List<Film> getAll();

    void addLike(Film film, User user);

    void deleteLike(Film film, User user);

    List<Film> getSortedFilmsByDirector(long directorId, String sortBy);

    List<Film> searchBy(String query, String by);

    List<Film> getTopPopular(int count, Long genreId, Integer year);
}
