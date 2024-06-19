package ru.yandex.practicum.filmorate.service.film;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.dto.FilmDto;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;
import java.util.Set;

@Service
public interface FilmService {

    FilmDto create(Film film);

    FilmDto update(Film film);

    List<FilmDto> getAll();

    void addLike(long userId, long filmId);

    void deleteLike(long userId, long filmId);

    Mpa getMpaById(int id);

    Set<Genre> getAllGenresByFilmId(int filmId);

    List<Film> getPopularFilms(int count);

    List<Film> getCommonFilms(int userId, int friendId);

    FilmDto getById(long id);
}
