package ru.yandex.practicum.filmorate.dao.genre;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

public interface GenreRepository {
    public List<Genre> findByIds(List<Long> ids) ;

    Genre getById(Long id);
}
