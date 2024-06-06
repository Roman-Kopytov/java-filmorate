package ru.yandex.practicum.filmorate.dao.mappers;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Component
public class FilmExtractor implements ResultSetExtractor<Optional<Film>> {

    @Override
    public Optional<Film> extractData(ResultSet rs) throws SQLException, DataAccessException {
        Film film = new Film();
        while (rs.next()) {
            film.setId(rs.getLong("id"));
            film.setName(rs.getString("name"));
            film.setDescription(rs.getString("description"));
            film.setDuration(rs.getInt("duration"));
            film.setReleaseDate(rs.getDate("releaseDate").toLocalDate());
            Array genreId = rs.get("genre_id");
            film.setGenreIds((
                    while st
                    Set<Integer>) );
            film.setMpa(rs.getInt("mpa"));
        }
        return Optional.of(film);
    }
}