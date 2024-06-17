package ru.yandex.practicum.filmorate.dao.mpa;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.mappers.MpaRowMapper;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class JdbcMpaRepository implements MpaRepository {
    private final NamedParameterJdbcOperations jdbcOperations;

    @Override
    public List<Mpa> findById(Integer id) {
        return jdbcOperations.query("SELECT * FROM mpa WHERE MPA.MPA_ID = :id",
                new MapSqlParameterSource("id", id), new MpaRowMapper());
    }

    @Override
    public List<Mpa> getAll() {
        return jdbcOperations.query("SELECT * FROM mpa", new MpaRowMapper());
    }

    @Override
    public Mpa getById(Integer id) {
        return jdbcOperations.queryForObject("SELECT * FROM mpa WHERE MPA.MPA_ID = :id",
                new MapSqlParameterSource("id", id), new MpaRowMapper());
    }


}
