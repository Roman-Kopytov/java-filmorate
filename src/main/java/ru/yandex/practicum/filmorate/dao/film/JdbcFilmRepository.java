package ru.yandex.practicum.filmorate.dao.film;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.mappers.FilmExtractor;
import ru.yandex.practicum.filmorate.dao.mappers.FilmRowMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Repository
@RequiredArgsConstructor
public class JdbcFilmRepository implements FilmRepository {

    private final NamedParameterJdbcOperations jdbcOperations;
    private final FilmExtractor filmExtractor;
    private final FilmRowMapper filmRowMapper;

    @Override
    public Optional<Film> getById(long filmId) {
        return jdbcOperations.query("SELECT * FROM films join mpa join genres WHERE film_id =:filmId",
                Map.of("filmId", filmId), filmExtractor);
    }

    @Override
    public Film save(Film film) {
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        Map<String, Object> map = Map.of("name", film.getName(),
                "description", film.getDescription(),
                "releaseDate", film.getReleaseDate(),
                "duration", film.getDuration(),
                "genre_id", film.getGenreId(),
                "rating_id", film.getMpa());
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValues(map);
        jdbcOperations.update("INSERT INTO films (name,description,releaseDate,duration,genre_id,rating_id)" +
                        " VALUES(:name,:description,:releaseDate,:duration,:genre_id,:rating_id)",
                params, keyHolder, new String[]{"film_id"});
        film.setId(keyHolder.getKeyAs(Long.class));
        return film;
    }

    @Override
    public Film update(Film film) {
        return null;
    }

    @Override
    public List<Film> getAll() {
        return jdbcOperations.query("SELECT * FROM films",filmRowMapper);
    }

    @Override
    public Map<Long, Set<Long>> getLikes() {
        return Map.of();
    }

    @Override
    public void addLike(Film film, User user) {

    }

    @Override
    public void deleteLike(Film film, User user) {

    }

    @Override
    public List<Film> getTopPopular(int count) {
        return List.of();
    }
}
