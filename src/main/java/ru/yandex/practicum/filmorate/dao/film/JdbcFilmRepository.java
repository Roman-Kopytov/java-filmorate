package ru.yandex.practicum.filmorate.dao.film;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.mappers.FilmExtractor;
import ru.yandex.practicum.filmorate.dao.mappers.FilmRowMapper;
import ru.yandex.practicum.filmorate.dao.mappers.GenreRowMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Repository
@RequiredArgsConstructor
public class JdbcFilmRepository implements FilmRepository {

    private final NamedParameterJdbcOperations jdbcOperations;
    private final FilmExtractor filmExtractor;
    private final FilmRowMapper filmRowMapper;

    @Override
    public Optional<Film> getById(long filmId) {
        return Optional.ofNullable(jdbcOperations.query("SELECT * FROM FILMS join MPA on(FILMS.MPA_ID=MPA.MPA_ID)" +
                        "join FILMS_GENRES on (FILMS.FILM_ID = FILMS_GENRES.FILM_ID) " +
                        "join GENRES on (FILMS_GENRES.GENRE_ID = GENRES.GENRE_ID) " +
                        "WHERE film_id =:filmId",
                Map.of("filmId", filmId), filmExtractor));
    }

    @Override
    public Film save(Film film) {
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        Map<String, Object> map = Map.of("name", film.getName(),
                "description", film.getDescription(),
                "releaseDate", film.getReleaseDate(),
                "duration", film.getDuration(),
                "MPA_ID", film.getMpa());
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValues(map);
        jdbcOperations.update("INSERT INTO FILMS (name,description,release_Date,duration,MPA_ID)" +
                        " VALUES(:name,:description,:releaseDate,:duration,:MPA_ID)",
                params, keyHolder, new String[]{"film_id"});

        film.setId(keyHolder.getKeyAs(Long.class));
        Map<String, Integer> batchValue = new HashMap<>();
        for (Genre genre : film.getGenres()) {
            batchValue.put("film_id", keyHolder.getKeyAs(Integer.class));
            batchValue.put("genre_id", genre.getId());
        }
        jdbcOperations.batchUpdate("INSERT INTO FILMS_GENRES (film_id,genre_id)",
                SqlParameterSourceUtils.createBatch(batchValue));
        return film;
    }

    @Override
    public Film update(Film film) {
        return null;
    }

    @Override
    public List<Film> getAll() {
        List<Genre> allGenres = jdbcOperations.query("SELECT * FROM GENRES", new GenreRowMapper());
        List<Film> allFilms = jdbcOperations.query("SELECT * FROM FILMS join MPA on(FILMS.MPA_ID=MPA.MPA_ID)",
                new FilmRowMapper());
        record GenreRelations(Long film_id, Integer genre_id) {
        }
        List<GenreRelations> genreRelations = jdbcOperations.queryForList("SELECT * FROM FILMS join FILMS_GENRES " +
                "on (FILMS.FILM_ID = FILMS_GENRES.FILM_ID)", Map.of(), GenreRelations.class);
        List<Film> films = new ArrayList<>();
        for (Film film : allFilms) {
            for (Genre)
            film.setGenres();
            film.
        }
        return
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
