package ru.yandex.practicum.filmorate.dao.film;

import lombok.RequiredArgsConstructor;
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
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class JdbcFilmRepository implements FilmRepository {

    private final NamedParameterJdbcOperations jdbcOperations;

    @Override
    public Film getById(long filmId) {
        return jdbcOperations.query("SELECT FILMS.FILM_ID, FILMS.NAME, FILMS.DESCRIPTION, FILMS.RELEASE_DATE, FILMS.DURATION, MPA.MPA_ID,MPA.NAME, GENRES.GENRE_ID, GENRES.NAME   FROM FILMS join MPA on FILMS.MPA_ID = MPA.MPA_ID join FILMS_GENRES on FILMS.FILM_ID = FILMS_GENRES.FILM_ID join GENRES on FILMS_GENRES.GENRE_ID = GENRES.GENRE_ID WHERE FILMS.film_id = :filmId",
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
        Map<String, Long> batchValue = new HashMap<>();
        for (Genre genre : film.getGenres()) {
            batchValue.put("film_id", keyHolder.getKeyAs(Long.class));
            batchValue.put("genre_id", genre.getId());
        }
        jdbcOperations.batchUpdate("INSERT INTO FILMS_GENRES (film_id,genre_id) VALUES (:film_id,:genre_id)",
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
        List<Film> allFilms = jdbcOperations.query("SELECT FILM_ID, FILMS.NAME, DESCRIPTION, RELEASE_DATE, DURATION, " +
                        "FILMS.MPA_ID, MPA.NAME FROM FILMS JOIN MPA on FILMS.MPA_ID = MPA.MPA_ID",
                new FilmRowMapper());
        record GenreRelations(Long film_id, Long genre_id) {
        }
//        List<GenreRelations> genreRelations = jdbcOperations.queryForList("SELECT FILMS.FILM_ID, FILMS_GENRES.GENRE_ID" +
//                " FROM FILMS JOIN FILMS_GENRES on FILMS.FILM_ID = FILMS_GENRES.FILM_ID", Map.of(), GenreRelations.class);
        List<GenreRelations> genreRelations = jdbcOperations.query("SELECT FILMS.FILM_ID, FILMS_GENRES.GENRE_ID" +
                " FROM FILMS JOIN FILMS_GENRES on FILMS.FILM_ID = FILMS_GENRES.FILM_ID", (rs, rowNum) ->
                new GenreRelations(rs.getLong("FILMS.FILM_ID"), rs.getLong("FILMS_GENRES.GENRE_ID")));
        List<Film> films = new ArrayList<>();
        HashMap<Long, Set<Genre>> filmMap = new HashMap<>();
        HashMap<Long, Genre> genreMap = new HashMap<>();
        allGenres.stream()
                .map(genre -> genreMap.put(genre.getId(), genre)).close();
        allFilms.stream()
                .map(film -> filmMap.put(film.getId(), new LinkedHashSet<>())).close();
        for (Long id : filmMap.keySet()) {
            filmMap.put(id, genreRelations.stream()
                    .filter(relation -> relation.film_id.equals(id))
                    .map(relation -> relation.genre_id)
                    .map(genreId -> genreMap.get(genreId))
                    .collect(Collectors.toSet()));

        }
        for (Film film : allFilms) {
            film.setGenres(filmMap.get(film.getId()));
        }
        return films;
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
