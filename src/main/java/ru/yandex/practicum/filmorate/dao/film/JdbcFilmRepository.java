package ru.yandex.practicum.filmorate.dao.film;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.mappers.FilmExtractor;
import ru.yandex.practicum.filmorate.dao.mappers.FilmRowMapper;
import ru.yandex.practicum.filmorate.dao.mappers.GenreRowMapper;
import ru.yandex.practicum.filmorate.dao.mappers.MpaRowMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Repository
@RequiredArgsConstructor
public class JdbcFilmRepository implements FilmRepository {

    private final NamedParameterJdbcOperations jdbcOperations;

    @Override
    public Film getById(long filmId) {
        return jdbcOperations.query("SELECT FILMS.FILM_ID, FILMS.NAME, FILMS.DESCRIPTION, FILMS.RELEASE_DATE, " +
                        "FILMS.DURATION, MPA.MPA_ID,MPA.NAME, GENRES.GENRE_ID, GENRES.NAME   " +
                        "FROM FILMS " +
                        "join MPA on FILMS.MPA_ID = MPA.MPA_ID " +
                        "left join FILMS_GENRES on FILMS.FILM_ID = FILMS_GENRES.FILM_ID " +
                        "left join GENRES on FILMS_GENRES.GENRE_ID = GENRES.GENRE_ID WHERE FILMS.film_id = :filmId",
                Map.of("filmId", filmId), new FilmExtractor());
    }

    @Override
    public Film save(Film film) {
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();

        Map<String, Object> map = Map.of("name", film.getName(),
                "description", film.getDescription(),
                "releaseDate", film.getReleaseDate(),
                "duration", film.getDuration(),
                "MPA_ID", film.getMpa().getId());
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValues(map);
        jdbcOperations.update("INSERT INTO FILMS (name,description,release_Date,duration,MPA_ID)" +
                        " VALUES(:name,:description,:releaseDate,:duration,:MPA_ID)",
                params, keyHolder, new String[]{"film_id"});

        film.setId(keyHolder.getKeyAs(Long.class));
        saveFilmGenres(film);
        return film;
    }

    private void saveFilmGenres(Film film) {
        if (film.getGenres().isEmpty()) {
            film.setGenres(null);
        }
        var batchValue = film.getGenres().stream()
                .map(genre -> new MapSqlParameterSource()
                        .addValue("film_id", film.getId())
                        .addValue("genre_id", genre.getId()))
                .toList();

        jdbcOperations.batchUpdate("INSERT INTO FILMS_GENRES (film_id,genre_id) VALUES (:film_id,:genre_id)",
                batchValue.toArray(new SqlParameterSource[0]));

    }


    private List<Mpa> getAllMpa() {
        return jdbcOperations.query("SELECT * FROM MPA", new MpaRowMapper());
    }


    private void cleanFilmGenres(Film film) {
        jdbcOperations.update("DELETE FROM FILMS_GENRES WHERE film_id = :filmId",
                Map.of("filmId", film.getId()));
    }

    @Override
    public Film update(Film film) {
        Map<String, Object> map = Map.of("filmId", film.getId(),
                "name", film.getName(),
                "description", film.getDescription(),
                "releaseDate", film.getReleaseDate(),
                "duration", film.getDuration(),
                "MPA_ID", film.getMpa().getId());
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValues(map);
        jdbcOperations.update("UPDATE FILMS " +
                " SET NAME=:name,DESCRIPTION=:description, RELEASE_DATE=:releaseDate,DURATION=:duration,MPA_ID=:MPA_ID " +
                "WHERE FILM_ID=:filmId", params);
        cleanFilmGenres(film);
        saveFilmGenres(film);
        return getById(film.getId());
    }

    @Override
    public List<Film> getAll() {
        final List<Genre> genres = getAllGenres();
        final List<Film> films = jdbcOperations.query("SELECT FILM_ID, FILMS.NAME, DESCRIPTION, RELEASE_DATE, DURATION, " +
                "FILMS.MPA_ID, MPA.NAME FROM FILMS JOIN MPA on FILMS.MPA_ID = MPA.MPA_ID", new FilmRowMapper());
        final Map<Long, Set<Genre>> filmGenres = getAllFilmsGenres(genres);

        films.forEach(film -> film.setGenres(filmGenres.getOrDefault(film.getId(), new HashSet<>())));
        return films;
    }

    Map<Long, Set<Genre>> getAllFilmsGenres(final List<Genre> allGenres) {
        final Map<Long, Set<Genre>> filmGenres = new HashMap<>();
        jdbcOperations.query("SELECT * FROM FILMS_GENRES", rs -> {
                    while (rs.next()) {
                        final long filmId = rs.getLong("film_id");
                        final long genreId = rs.getLong("genre_id");
                        final Genre genre = allGenres.stream()
                                .filter(g -> g.getId() == genreId)
                                .findFirst()
                                .get();
                        filmGenres.computeIfAbsent(filmId, k -> new HashSet<>()).add(genre);
                    }
                }
        );
        return filmGenres;
    }

    public List<Genre> getAllGenres() {
        return jdbcOperations.query("SELECT * FROM GENRES", new GenreRowMapper());
    }


    @Override
    public Map<Long, Set<Long>> getLikes() {
        return Map.of();
    }

    @Override
    public void addLike(Film film, User user) {
        jdbcOperations.update("INSERT INTO LIKES (film_id,user_id) VALUES (:film_id,:user_id)",
                Map.of("film_id", film.getId(), "user_id", user.getId()));
    }

    @Override
    public void deleteLike(Film film, User user) {
        jdbcOperations.update("DELETE FROM LIKES WHERE FILM_ID=:film_id",
                Map.of("film_id", film.getId()));
    }

    @Override
    public List<Film> getTopPopular(int count) {
        final List<Genre> genres = getAllGenres();
        final List<Film> films = jdbcOperations.query("SELECT FILMS.FILM_ID, FILMS.NAME, DESCRIPTION, RELEASE_DATE, DURATION, " +
                        "FILMS.MPA_ID, MPA.NAME " +
                        "FROM FILMS " +
                        "JOIN MPA on FILMS.MPA_ID = MPA.MPA_ID " +
                        "LEFT JOIN LIKES on FILMS.FILM_ID = LIKES.FILM_ID " +
                        "GROUP BY FILMS.FILM_ID " +
                        "ORDER BY COUNT(LIKES.USER_ID) desc " +
                        "LIMIT :count",
                Map.of("count", count), new FilmRowMapper());
        final Map<Long, Set<Genre>> filmGenres = getAllFilmsGenres(genres);

        films.forEach(film -> film.setGenres(filmGenres.getOrDefault(film.getId(), new HashSet<>())));
        return films;
    }
}
