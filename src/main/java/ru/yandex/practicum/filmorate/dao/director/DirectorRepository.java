package ru.yandex.practicum.filmorate.dao.director;

import ru.yandex.practicum.filmorate.model.Director;

import java.util.List;

public interface DirectorRepository {
    List<Director> getAll();

    Director getById(long id);

    Director create(Director director);

    Director update(Director director);

    void delete(long id);
}
