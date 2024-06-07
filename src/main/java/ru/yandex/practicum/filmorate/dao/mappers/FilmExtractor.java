package ru.yandex.practicum.filmorate.dao.mappers;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Component
public class FilmExtractor implements ResultSetExtractor<Film> {

    @Override
    public Film extractData(ResultSet rs) throws SQLException, DataAccessException {
        Film film = new Film();
        Set<Genre> genresSet = new LinkedHashSet<>();
        film.setId(rs.getLong("id"));
        film.setName(rs.getString("name"));
        film.setDescription(rs.getString("description"));
        film.setDuration(rs.getLong("duration"));
        film.setReleaseDate(rs.getDate("releaseDate").toLocalDate());
        film.setMpa(new Mpa(rs.getInt("MPA.MPA_ID"), rs.getString("MPA.NAME")));
        while (rs.next()) {
            genresSet.add(new Genre(rs.getInt("GENRES.genre_id"),
                    rs.getString("GENRES.name")));
            film.setGenres(genresSet);
        }
        return film;
    }
}