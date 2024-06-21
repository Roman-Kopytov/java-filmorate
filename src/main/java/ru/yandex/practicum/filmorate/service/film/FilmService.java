package ru.yandex.practicum.filmorate.service.film;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.dto.FilmDto;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

@Service
public interface FilmService {

    FilmDto create(Film film);

    FilmDto update(Film film);

    List<FilmDto> getAll();

    void addLike(long userId, long filmId);

    void deleteLike(long userId, long filmId);

    List<Film> getPopularFilms(int count);

    List<FilmDto> getDirectorFilmsSortedBy(long directorId, String sortBy);

    FilmDto getById(long id);

    List<FilmDto> searchBy(String query, String by);
}
