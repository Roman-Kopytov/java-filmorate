package ru.yandex.practicum.filmorate.dao.film;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.mappers.DirectorRowMapper;
import ru.yandex.practicum.filmorate.dao.mappers.FilmExtractor;
import ru.yandex.practicum.filmorate.dao.mappers.FilmRowMapper;
import ru.yandex.practicum.filmorate.dao.mappers.GenreRowMapper;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Repository
@RequiredArgsConstructor
public class JdbcFilmRepository implements FilmRepository {

    private final NamedParameterJdbcOperations jdbcOperations;

    @Override
    public Optional<Film> getById(long filmId) {
        return Optional.ofNullable(jdbcOperations.query(
                """
                        SELECT FILMS.FILM_ID, FILMS.NAME, FILMS.DESCRIPTION, FILMS.RELEASE_DATE,
                        FILMS.DURATION, MPA.MPA_ID, MPA.NAME,
                        DIRECTORS.DIRECTOR_ID, DIRECTORS.NAME, GENRES.GENRE_ID, GENRES.NAME
                        FROM FILMS
                        join MPA on FILMS.MPA_ID = MPA.MPA_ID
                        left join FILMS_GENRES on FILMS.FILM_ID = FILMS_GENRES.FILM_ID
                        left join GENRES on FILMS_GENRES.GENRE_ID = GENRES.GENRE_ID
                        left join FILM_DIRECTORS on FILMS.FILM_ID = FILM_DIRECTORS.FILM_ID
                        left join DIRECTORS on FILM_DIRECTORS.DIRECTOR_ID = DIRECTORS.DIRECTOR_ID
                        WHERE FILMS.film_id = :filmId
                        """,
                Map.of("filmId", filmId), new FilmExtractor()));
    }

    @Override
    public void deleteFilmById(long id) {
        jdbcOperations.update("""
                DELETE FROM FILMS
                WHERE FILM_ID = :filmId
                """, Map.of("filmId", id));
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
        jdbcOperations.update(
                """
                        INSERT INTO FILMS (name,description,release_Date,duration,MPA_ID)
                        VALUES(:name,:description,:releaseDate,:duration,:MPA_ID)
                        """,
                params, keyHolder, new String[]{"film_id"});

        film.setId(keyHolder.getKeyAs(Long.class));
        saveFilmGenres(film);
        saveDirectorsToFilm(film);
        return film;
    }

    private void saveFilmGenres(Film film) {
        if (film.getGenres() == null) {
            film.setGenres(null);
            return;
        }
        var batchValue = film.getGenres().stream()
                .map(genre -> new MapSqlParameterSource()
                        .addValue("film_id", film.getId())
                        .addValue("genre_id", genre.getId()))
                .toList();

        jdbcOperations.batchUpdate("INSERT INTO FILMS_GENRES (film_id,genre_id) VALUES (:film_id,:genre_id)",
                batchValue.toArray(new SqlParameterSource[0]));

    }


    private void cleanFilmGenres(Film film) {
        jdbcOperations.update("DELETE FROM FILMS_GENRES WHERE film_id = :filmId",
                Map.of("filmId", film.getId()));
    }

    private void saveDirectorsToFilm(Film film) {
        if (film.getDirectors() == null) {
            film.setDirectors(null);
            return;
        } else if (film.getDirectors().isEmpty()) {
            film.setDirectors(new HashSet<>());
        }
        var batchValue = film.getDirectors().stream()
                .map(director -> new MapSqlParameterSource()
                        .addValue("film_id", film.getId())
                        .addValue("director_id", director.getId()))
                .toList();

        jdbcOperations.batchUpdate("INSERT INTO FILM_DIRECTORS (FILM_ID,DIRECTOR_ID) VALUES (:film_id,:director_id)",
                batchValue.toArray(new SqlParameterSource[0]));
    }

    private void cleanDirectorsFromFilm(Film film) {
        jdbcOperations.update("DELETE FROM FILM_DIRECTORS WHERE film_id = :filmId",
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
        jdbcOperations.update(
                """
                        UPDATE FILMS
                        SET NAME=:name,DESCRIPTION=:description, RELEASE_DATE=:releaseDate,DURATION=:duration,MPA_ID=:MPA_ID
                        WHERE FILM_ID=:filmId
                        """, params);
        Film oldFilm = getById(film.getId()).get();
        cleanFilmGenres(film);
        if (film.getGenres() == null) {
            film.setGenres(oldFilm.getGenres());
        } else if (film.getGenres().isEmpty()) {
            film.setGenres(new LinkedHashSet<>());
        }
        saveFilmGenres(film);
        cleanDirectorsFromFilm(film);
        saveDirectorsToFilm(film);
        return film;
    }

    @Override
    public List<Film> getAll() {
        final List<Film> films = jdbcOperations.query(
                """
                        SELECT FILM_ID, FILMS.NAME, DESCRIPTION, RELEASE_DATE,
                        DURATION,FILMS.MPA_ID, MPA.NAME FROM FILMS JOIN MPA on FILMS.MPA_ID = MPA.MPA_ID
                        """, new FilmRowMapper());
        return collectFilmComponent(films);
    }

    Map<Long, LinkedHashSet<Genre>> getAllFilmsGenres(final List<Genre> allGenres) {
        final Map<Long, LinkedHashSet<Genre>> filmGenres = new HashMap<>();
        jdbcOperations.query("SELECT * FROM FILMS_GENRES", rs -> {

                    final long filmId = rs.getLong("film_id");
                    final long genreId = rs.getLong("genre_id");
                    final Genre genre = allGenres.stream()
                            .filter(g -> g.getId() == genreId)
                            .findFirst()
                            .get();
                    filmGenres.computeIfAbsent(filmId, k -> new LinkedHashSet<>()).add(genre);
                }
        );

        return filmGenres;
    }

    Map<Long, HashSet<Director>> getDirectorsByFilmMap(final List<Director> allDirectors) {
        final Map<Long, HashSet<Director>> filmDirectors = new HashMap<>();
        jdbcOperations.query("SELECT * FROM FILM_DIRECTORS", rs -> {
                    final long filmId = rs.getLong("film_id");
                    final long directorId = rs.getLong("director_id");
                    final Director director = allDirectors.stream()
                            .filter(g -> g.getId() == directorId)
                            .findFirst()
                            .get();
                    filmDirectors.computeIfAbsent(filmId, k -> new LinkedHashSet<>()).add(director);
                }
        );
        return filmDirectors;
    }

    private List<Genre> getAllGenres() {
        return jdbcOperations.query("SELECT * FROM GENRES", new GenreRowMapper());
    }

    private List<Director> getAllDirectors() {
        return jdbcOperations.query("SELECT * FROM DIRECTORS", new DirectorRowMapper());
    }

    @Override
    public void addLike(Film film, User user) {
        jdbcOperations.update("INSERT INTO LIKES (film_id,user_id) VALUES (:film_id,:user_id)",
                Map.of("film_id", film.getId(), "user_id", user.getId()));

        saveEvent(user.getId(), film.getId(), "LIKE", "ADD");
    }

    @Override
    public void deleteLike(Film film, User user) {
        jdbcOperations.update("DELETE FROM LIKES WHERE FILM_ID=:film_id",
                Map.of("film_id", film.getId()));

        saveEvent(user.getId(), film.getId(), "LIKE", "REMOVE");
    }

    private void saveEvent(long userId, long entityId, String eventType, String operation) {
        Map<String, Object> eventValues = new HashMap<>();
        eventValues.put("userId", userId);
        eventValues.put("entityId", entityId);
        eventValues.put("eventType", eventType);
        eventValues.put("operation", operation);

        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        MapSqlParameterSource params = new MapSqlParameterSource(eventValues);
        String query = "INSERT INTO FEED (USER_ID,ENTITY_ID,EVENT_TYPE,OPERATION)" +
                " VALUES(:userId,:entityId,:eventType,:operation)";
        jdbcOperations.update(query, params, keyHolder);
    }

    @Override
    public List<Film> getTopPopular(int count, Long genreId, Integer year) {
        String query;
        List<Film> films;
        if (year == null && genreId == null) {
            query = """
                    SELECT FILMS.FILM_ID, FILMS.NAME, DESCRIPTION, RELEASE_DATE, DURATION,
                    FILMS.MPA_ID, MPA.NAME
                    FROM FILMS
                    LEFT JOIN MPA on FILMS.MPA_ID = MPA.MPA_ID
                    LEFT JOIN LIKES on FILMS.FILM_ID = LIKES.FILM_ID
                    GROUP BY FILMS.FILM_ID
                    ORDER BY COUNT(LIKES.USER_ID) desc
                    LIMIT :count
                    """;
            films = jdbcOperations.query(query, Map.of("count", count), new FilmRowMapper());
        } else if (year != null && genreId == null) {
            query = """
                    SELECT f.FILM_ID, f.NAME, f.DESCRIPTION, f.RELEASE_DATE, f.DURATION, f.MPA_ID, m.NAME
                    FROM FILMS f
                    LEFT JOIN MPA m on f.MPA_ID = m.MPA_ID
                    LEFT JOIN LIKES l on f.FILM_ID = l.FILM_ID
                    WHERE EXTRACT (YEAR FROM f.RELEASE_DATE) = :year
                    GROUP BY f.FILM_ID
                    ORDER BY COUNT(l.USER_ID) DESC
                    LIMIT :count
                    """;
            films = jdbcOperations.query(query, Map.of("count", count, "year", year), new FilmRowMapper());
        } else if (genreId != null && year == null) {
            query = """
                    SELECT f.*, m.name FROM films f
                    LEFT JOIN MPA m on f.MPA_ID = m.MPA_ID
                    LEFT JOIN LIKES l on f.FILM_ID = l.FILM_ID
                    LEFT JOIN FILMS_GENRES fg ON f.FILM_ID =fg.FILM_ID
                    LEFT JOIN genres g ON fg.GENRE_ID = g.GENRE_ID
                    WHERE g.GENRE_ID =:genreId
                    GROUP BY f.FILM_ID
                    ORDER BY COUNT(l.USER_ID) DESC
                    LIMIT :count
                    """;
            films = jdbcOperations.query(query,
                    Map.of("genreId", genreId, "count", count), new FilmRowMapper());
        } else {
            query = """
                    SELECT f.*, m.MPA_ID, m.NAME FROM films f
                    LEFT JOIN MPA m on f.MPA_ID = m.MPA_ID
                    LEFT JOIN LIKES l on f.FILM_ID = l.FILM_ID
                    LEFT JOIN FILMS_GENRES fg ON f.FILM_ID =fg.FILM_ID
                    LEFT JOIN genres g ON fg.GENRE_ID = g.GENRE_ID
                    WHERE g.GENRE_ID =:genreId AND EXTRACT (YEAR FROM f.RELEASE_DATE) =:year
                    GROUP BY f.FILM_ID
                    ORDER BY COUNT(l.USER_ID) DESC
                    LIMIT :count
                    """;
            films = jdbcOperations.query(query,
                    Map.of("genreId", genreId, "year", year, "count", count), new FilmRowMapper());
        }
        return collectFilmComponent(films);
    }

    @Override
    public List<Film> getSortedFilmsByDirector(long directorId, String sortBy) {
        final List<Film> films;
        String query = """
                SELECT FILMS.FILM_ID, FILMS.NAME, DESCRIPTION, RELEASE_DATE, DURATION,
                FILMS.MPA_ID, MPA.NAME, FILM_DIRECTORS.DIRECTOR_ID
                FROM FILMS
                JOIN MPA on FILMS.MPA_ID = MPA.MPA_ID
                LEFT JOIN LIKES on FILMS.FILM_ID = LIKES.FILM_ID
                LEFT JOIN FILM_DIRECTORS on FILMS.FILM_ID = FILM_DIRECTORS.FILM_ID
                WHERE FILM_DIRECTORS.DIRECTOR_ID = :directorId
                GROUP BY FILMS.FILM_ID
                """ + ((sortBy.equals("likes") ?
                "\nORDER BY COUNT(LIKES.USER_ID) desc" : "ORDER BY FILMS.RELEASE_DATE asc"));
        films = jdbcOperations.query(query, Map.of("directorId", directorId), new FilmRowMapper());
        return collectFilmComponent(films);
    }

    @Override
    public List<Film> searchBy(String query, String by) {
        return switch (by) {
            case "title" -> collectFilmComponent(jdbcOperations.query("""
                    SELECT FILMS.FILM_ID, FILMS.NAME, DESCRIPTION, RELEASE_DATE, DURATION, FILMS.MPA_ID, MPA.NAME, DIRECTORS.NAME
                    FROM FILMS
                    JOIN MPA on FILMS.MPA_ID = MPA.MPA_ID
                    LEFT JOIN FILM_DIRECTORS on FILMS.FILM_ID = FILM_DIRECTORS.FILM_ID
                    LEFT JOIN DIRECTORS on DIRECTORS.DIRECTOR_ID = FILM_DIRECTORS.DIRECTOR_ID
                    LEFT JOIN LIKES on FILMS.FILM_ID = LIKES.FILM_ID
                    WHERE LOWER(FILMS.NAME) LIKE  LOWER(concat('%', :query, '%'))
                    GROUP BY FILMS.FILM_ID
                    ORDER BY COUNT(LIKES.USER_ID) desc
                    """, Map.of("query", query), new FilmRowMapper()));
            case "director" -> collectFilmComponent(jdbcOperations.query("""
                    SELECT FILMS.FILM_ID, FILMS.NAME, DESCRIPTION, RELEASE_DATE, DURATION, FILMS.MPA_ID, MPA.NAME,DIRECTORS.NAME
                    FROM FILMS
                    JOIN MPA on FILMS.MPA_ID = MPA.MPA_ID
                    LEFT JOIN FILM_DIRECTORS on FILMS.FILM_ID = FILM_DIRECTORS.FILM_ID
                    LEFT JOIN DIRECTORS on DIRECTORS.DIRECTOR_ID = FILM_DIRECTORS.DIRECTOR_ID
                    LEFT JOIN LIKES on FILMS.FILM_ID = LIKES.FILM_ID
                    WHERE LOWER(DIRECTORS.NAME) LIKE LOWER(concat('%', :query, '%'))
                    GROUP BY FILMS.FILM_ID
                    ORDER BY COUNT(LIKES.USER_ID) desc
                    """, Map.of("query", query), new FilmRowMapper()));
            case "director,title" -> collectFilmComponent(jdbcOperations.query("""
                    SELECT FILMS.FILM_ID, FILMS.NAME, DESCRIPTION, RELEASE_DATE, DURATION, FILMS.MPA_ID, MPA.NAME,DIRECTORS.NAME
                    FROM FILMS
                    JOIN MPA on FILMS.MPA_ID = MPA.MPA_ID
                    LEFT JOIN FILM_DIRECTORS on FILMS.FILM_ID = FILM_DIRECTORS.FILM_ID
                    LEFT JOIN DIRECTORS on DIRECTORS.DIRECTOR_ID = FILM_DIRECTORS.DIRECTOR_ID
                    LEFT JOIN LIKES on FILMS.FILM_ID = LIKES.FILM_ID
                    WHERE LOWER(DIRECTORS.NAME) LIKE LOWER(concat('%', :query, '%')) OR LOWER(FILMS.NAME) LIKE LOWER(concat('%', :query, '%'))
                    GROUP BY FILMS.FILM_ID
                    ORDER BY COUNT(LIKES.USER_ID) desc
                    """, Map.of("query", query), new FilmRowMapper()));
            default -> null;
        };
    }

    @Override
    public List<Film> getCommonFilms(long userId, long friendId) {
        return collectFilmComponent(jdbcOperations.query("""
                SELECT f.FILM_ID AS id, f.NAME, f.DESCRIPTION, f.RELEASE_DATE, f.DURATION,
                f.MPA_ID, m.NAME AS mpa_name
                FROM FILMS AS f
                JOIN MPA AS m ON m.MPA_ID = f.MPA_ID
                JOIN LIKES AS l ON f.FILM_ID = l.FILM_ID
                JOIN LIKES AS lf ON f.FILM_ID = lf.FILM_ID
                WHERE l.USER_ID = :userId AND lf.USER_ID = :friendId
                GROUP BY f.FILM_ID, f.NAME, f.DESCRIPTION, f.RELEASE_DATE, f.DURATION, f.MPA_ID, m.NAME
                ORDER BY f.NAME
                """, Map.of("userId", userId, "friendId", friendId), new FilmRowMapper()));
    }

    private List<Film> collectFilmComponent(List<Film> films) {
        final List<Genre> genres = getAllGenres();
        final List<Director> directors = getAllDirectors();

        final Map<Long, LinkedHashSet<Genre>> filmGenres = getAllFilmsGenres(genres);
        final Map<Long, HashSet<Director>> filmDirectors = getDirectorsByFilmMap(directors);

        films.forEach(film -> film.setGenres(filmGenres.getOrDefault(film.getId(), new LinkedHashSet<>())));
        films.forEach(film -> film.setDirectors(filmDirectors.getOrDefault(film.getId(), new HashSet<>())));
        return films;
    }
}
