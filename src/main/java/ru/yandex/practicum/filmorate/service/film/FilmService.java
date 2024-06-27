package ru.yandex.practicum.filmorate.service.film;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.dto.FilmDto;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.SortedBy;

import java.util.List;

@Service
public interface FilmService {

    FilmDto create(Film film);

    FilmDto update(Film film);

    List<FilmDto> getAll();

    void addLike(long userId, long filmId);

    void deleteLike(long userId, long filmId);

    List<FilmDto> getCommonFilms(long userId, long friendId);

    List<FilmDto> getDirectorFilmsSortedBy(long directorId, SortedBy sortBy);

    FilmDto getById(long id);

    FilmDto deleteFilmById(long id);

    List<FilmDto> searchBy(String query, String by);

    List<FilmDto> getPopularFilms(int count, Long genreId, Integer year);
}
