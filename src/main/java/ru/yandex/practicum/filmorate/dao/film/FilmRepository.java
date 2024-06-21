package ru.yandex.practicum.filmorate.dao.film;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface FilmRepository {

    Film getById(long filmId);

    Film save(Film film);

    Film update(Film film);

    List<Film> getAll();

    Map<Long, Set<Long>> getLikes();

    void addLike(Film film, User user);

    void deleteLike(Film film, User user);

    List<Film> getTopPopular(int count);

    List<Film> searchByTitle(String query, String by);

    List<Film> searchByDirectorAndTitle(String query, String by);

    List<Film> searchByDirector(String query, String by);

    List<Film> getSortedFilmsByDirector(long directorId, String sortBy);
}
