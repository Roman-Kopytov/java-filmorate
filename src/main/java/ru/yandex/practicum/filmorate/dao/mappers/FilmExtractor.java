package ru.yandex.practicum.filmorate.dao.mappers;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.LinkedHashSet;

@Component
public class FilmExtractor implements ResultSetExtractor<Film> {

    @Override
    public Film extractData(ResultSet rs) throws SQLException, DataAccessException {
        if (!rs.isBeforeFirst()) {
            return null;
        }
        LinkedHashSet<Genre> genresSet = new LinkedHashSet<>();
        HashSet<Director> directors = new HashSet<>();
        Film film = new Film();
        while (rs.next()) {
            film.setId(rs.getLong("FILM_ID"));
            film.setName(rs.getString("FILMS.NAME"));
            film.setDescription(rs.getString("FILMS.DESCRIPTION"));
            film.setDuration(rs.getLong("FILMS.DURATION"));
            film.setReleaseDate(rs.getDate("FILMS.RELEASE_DATE").toLocalDate());
            film.setMpa(new Mpa(rs.getString("MPA.NAME"), rs.getInt("MPA.MPA_ID")));
            Long genreId = rs.getLong("GENRES.GENRE_ID");
            if (!rs.wasNull()) {
                genresSet.add(new Genre(genreId, rs.getString("GENRES.NAME")));
            }
            Long directorId = rs.getLong("DIRECTORS.DIRECTOR_ID");
            if (!rs.wasNull()) {
                directors.add(new Director(directorId, rs.getString("DIRECTORS.NAME")));
            }
        }

        film.setGenres(genresSet);
        film.setDirectors(directors);
        return film;
    }
}
