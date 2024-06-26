package ru.yandex.practicum.filmorate.dao.director;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.mappers.DirectorRowMapper;
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

    @Override
    public List<Director> getAll() {
        log.info("Выполняется возврат всех режиссеров из БД");
        return jdbc.query("SELECT * FROM DIRECTORS", directorRowMapper);
    }

    @Override
    public Director getById(long id) {
        log.info("Выполняется возврат режиссера с id {} из БД", id);
        return jdbc.queryForObject("SELECT * FROM DIRECTORS WHERE DIRECTOR_ID = ?", directorRowMapper, id);
    }

    @Override
    public Director create(Director director) {

        Map<String, Object> columns = new SimpleJdbcInsert(jdbc)
                .withTableName("DIRECTORS")
                .usingGeneratedKeyColumns("DIRECTOR_ID")
                .executeAndReturnKeyHolder(Map.of("name", director.getName()))
                .getKeys();

        log.info("Создан новый режиссер с id {}", columns.get("DIRECTOR_ID"));
        director.setId((Long) columns.get("DIRECTOR_ID"));
        return director;
    }

    @Override
    public Director update(Director director) {
        int rowsUpdated = jdbc.update("UPDATE DIRECTORS SET NAME = ? WHERE DIRECTOR_ID = ?",
                director.getName(), director.getId());
        if (rowsUpdated == 0) {
            throw new NotFoundException("This Director_Id" + director.getId() + " not contains in DATA");
        }
        return director;
    }

    @Override
    public void delete(long id) {
        jdbc.update("DELETE FROM DIRECTORS WHERE DIRECTOR_ID = ?", id);
    }
}
