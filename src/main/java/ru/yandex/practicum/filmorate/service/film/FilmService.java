package ru.yandex.practicum.filmorate.service.film;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.Collection;

@Service
public interface FilmService {

    Film create(Film film);

    Film update(Film film);

    Film get(long filmId);

    ArrayList<Film> getAll();

    void addLike(long userId, long filmId);

    void deleteLike(long userId, long filmId);

    Collection<Film> getPopularFilms(int count);
}
