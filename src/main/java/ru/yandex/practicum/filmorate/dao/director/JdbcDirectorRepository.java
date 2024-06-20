package ru.yandex.practicum.filmorate.dao.director;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.mappers.DirectorRowMapper;
import ru.yandex.practicum.filmorate.exception.BadBodyRequestException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Director;

import java.util.List;
import java.util.Map;

@Repository
@Slf4j
public class JdbcDirectorRepository implements DirectorRepository {
    private final JdbcTemplate jdbc;
    private final DirectorRowMapper directorRowMapper;

    @Autowired
    public JdbcDirectorRepository(JdbcTemplate jdbc, DirectorRowMapper directorRowMapper) {
        this.jdbc = jdbc;
        this.directorRowMapper = directorRowMapper;
    }

    public List<Director> getAll() {
        log.info("Выполняется возврат всех режиссеров из БД");
        return jdbc.query("SELECT * FROM DIRECTORS", directorRowMapper);
    }

    public Director getById(long id) {
        log.info("Выполняется возврат режиссера с id {} из БД", id);
        try {
            return jdbc.queryForObject("SELECT * FROM DIRECTORS WHERE DIRECTOR_ID = ?", directorRowMapper, id);
        } catch (EmptyResultDataAccessException e) {
            log.error("Пользователь попытался получить информацию о несуществующим режиссере с id {}", id);
            throw new NotFoundException(String.format("Режиссера с id %l еще нет", id));
        }
    }

    public Director create(Director director) {
        SimpleJdbcInsert insert = new SimpleJdbcInsert(jdbc).withTableName("DIRECTORS").usingGeneratedKeyColumns("DIRECTOR_ID");
        long id = insert.executeAndReturnKey(Map.of("name", director.getName())).longValue();

        log.info("Создан новый режиссер с id {}", id);
        return new Director(id, director.getName());
    }

    public Director update(Director director) {
        int rowsUpdated = jdbc.update("UPDATE DIRECTORS SET NAME = ? WHERE DIRECTOR_ID = ?",
                director.getName(), director.getId());
        if (rowsUpdated == 0) {
            log.error("Пользователь попытался обновить информацию о несуществующем режиссере с id {}", director.getId());
            throw new BadBodyRequestException("Режиссера с таким id еще нет");
        }

        return director;
    }

    public void delete(long id) {
        jdbc.update("DELETE FROM DIRECTORS WHERE DIRECTOR_ID = ?", id);
    }
}