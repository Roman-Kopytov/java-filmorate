package ru.yandex.practicum.filmorate.dao.mappers;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class FilmRowMapper implements RowMapper<Film> {
    @Override
    public Film mapRow(ResultSet rs, int rowNum) throws SQLException {
        Film film = new Film();
        film.setId(rs.getLong("FILM_ID"));
        film.setName(rs.getString("FILMS.NAME"));
        film.setDescription(rs.getString("DESCRIPTION"));
        film.setDuration(rs.getLong("DURATION"));
        film.setReleaseDate(rs.getDate("RELEASE_DATE").toLocalDate());
        film.setMpa(new Mpa(rs.getInt("MPA_ID"), rs.getString("MPA.NAME")));
        film.setDirector(new Director(rs.getLong("DIRECTOR_ID"), rs.getString("DIRECTORS.NAME")));
        return film;
    }
}
