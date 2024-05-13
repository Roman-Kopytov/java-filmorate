package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public interface FilmRepository {

    Optional<Film> get(long filmId);

    Film create(Film film);

    Film update(Film film);

    ArrayList<Film> getAll();

    Map<Long, Set<Long>> getLikes();

    void addLike(Film film, User user);

    void deleteLike(Film film, User user);
}
