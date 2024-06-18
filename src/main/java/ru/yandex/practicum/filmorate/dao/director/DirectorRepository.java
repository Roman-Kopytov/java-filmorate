package ru.yandex.practicum.filmorate.dao.director;

import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import ru.yandex.practicum.filmorate.exception.BadBodyRequestException;
import ru.yandex.practicum.filmorate.model.Director;

import java.util.List;

public interface DirectorRepository {
    public List<Director> getAll();

    public Director getById(long id);

    public Director create(Director director);

    public Director update(Director director);

    public void delete(long id);
}
