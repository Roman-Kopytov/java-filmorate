package ru.yandex.practicum.filmorate.dao.genre;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

public interface GenreRepository {
    List<Genre> findByIds(List<Long> ids);

    Genre getById(Long id);

    List<Genre> getAll();
}
