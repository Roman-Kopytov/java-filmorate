package ru.yandex.practicum.filmorate.dao.genre;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.mappers.GenreRowMapper;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class JdbcGenreRepository implements GenreRepository {
    private final NamedParameterJdbcOperations jdbcOperations;

    @Override
    public List<Genre> findByIds(List<Long> ids) {
        return jdbcOperations.query("SELECT * FROM GENRES WHERE GENRE_ID IN (:ids)",
                new MapSqlParameterSource("ids", ids), new GenreRowMapper());
    }

    @Override
    public Genre getById(Long id) {
        return jdbcOperations.queryForObject("SELECT * FROM GENRES WHERE GENRE_ID = :id",
                new MapSqlParameterSource("id", id), new GenreRowMapper());
    }

    @Override
    public List<Genre> getAll() {
        return jdbcOperations.query("SELECT * FROM GENRES",
                new GenreRowMapper());
    }

}
