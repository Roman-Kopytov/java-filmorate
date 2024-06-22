package ru.yandex.practicum.filmorate.dao.film;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface FilmRepository {

    Film getById(long filmId);

    Film save(Film film);

    Film update(Film film);

    List<Film> getAll();

    void addLike(Film film, User user);

    void deleteLike(Film film, User user);

    List<Film> getTopPopular(int count);

    List<Film> getSortedFilmsByDirector(long directorId, String sortBy);

    List<Film> searchBy(String query, String by);
}
