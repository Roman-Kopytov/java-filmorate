package ru.yandex.practicum.filmorate.service.film;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.dao.dto.FilmDto;
import ru.yandex.practicum.filmorate.dao.film.FilmRepository;
import ru.yandex.practicum.filmorate.dao.film.JdbcFilmRepository;
import ru.yandex.practicum.filmorate.dao.genre.GenreRepository;
import ru.yandex.practicum.filmorate.dao.mappers.FilmRowMapper;
import ru.yandex.practicum.filmorate.dao.mpa.MpaRepository;
import ru.yandex.practicum.filmorate.dao.user.UserRepository;
import ru.yandex.practicum.filmorate.exception.BadBodyRequestException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.mapper.FilmMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import java.util.stream.Collectors;

import static ru.yandex.practicum.filmorate.mapper.FilmMapper.mapToUserDto;

@Service
@RequiredArgsConstructor
public class GeneralFilmService implements FilmService {

    private final FilmRepository filmRepository;
    private final UserRepository userRepository;
    private final GenreRepository genreRepository;
    private final MpaRepository mpaRepository;
    private final JdbcTemplate jdbcTemplate;
    private final FilmRowMapper filmRowMapper;
    private final JdbcFilmRepository jdbcFilmRepository;


    @Override
    public FilmDto getById(long id) {
        return mapToUserDto(getFilmFromRepository(id));

    }

    @Override
    public List<FilmDto> searchBy(String query, String by) {
        return filmRepository.searchBy(query, by).stream()
                .map(FilmMapper::mapToUserDto)
                .collect(Collectors.toList());
    }


    @Override
    public FilmDto create(Film film) {
        if (isGenresValid(film) && isMpaValid(film)) {
            return mapToUserDto(filmRepository.save(film));
        }
        return null;
    }

    @Override
    public FilmDto update(Film film) {
        if (isGenresValid(film) && isMpaValid(film)) {
            long filmId = film.getId();
            getFilmFromRepository(filmId);
            return mapToUserDto(filmRepository.update(film));
        }
        return null;
    }

    @Override
    public List<FilmDto> getAll() {
        return filmRepository.getAll().stream()
                .map(FilmMapper::mapToUserDto)
                .collect(Collectors.toList());
    }

    @Override
    public void addLike(long userId, long filmId) {
        filmRepository.addLike(getFilmFromRepository(filmId), getUserFromRepository(userId));
    }

    @Override
    public void deleteLike(long userId, long filmId) {
        filmRepository.deleteLike(getFilmFromRepository(filmId), getUserFromRepository(userId));
    }

    @Override
    public List<Film> getCommonFilms(long userId, long friendId) {
        String sqlQuery = " SELECT f.FILM_ID AS id, f.NAME, f.DESCRIPTION, f.RELEASE_DATE, f.DURATION, " +
                "f.MPA_ID, m.NAME AS mpa_name " +
                "FROM FILMS AS f " +
                "JOIN MPA AS m ON m.MPA_ID = f.MPA_ID " +
                "JOIN LIKES AS l ON f.FILM_ID = l.FILM_ID " +
                "JOIN LIKES AS lf ON f.FILM_ID = lf.FILM_ID " +
                "WHERE l.USER_ID = ? AND lf.USER_ID = ? " +
                "GROUP BY f.FILM_ID, f.NAME, f.DESCRIPTION, f.RELEASE_DATE, f.DURATION, f.MPA_ID, m.NAME " +
                "ORDER BY f.NAME";

        return jdbcTemplate.query(sqlQuery, this::mapRowToFilmWithGenres, userId, friendId);
    }

    private Film mapRowToFilmWithGenres(ResultSet rs, int rowNum) throws SQLException {
        Film film = mapRowToFilm(rs,rowNum);
        film.setGenres(getAllGenresByFilmId(rs.getInt("id")));
        return film;
    }

    @Override
    public LinkedHashSet<Genre> getAllGenresByFilmId(int filmId) {
        String sqlQuery = "SELECT fg.GENRE_ID AS id, g.NAME AS name \n" +
                "FROM FILMS_GENRES AS fg\n" +
                "INNER JOIN GENRES AS g ON fg.GENRE_ID = g.GENRE_ID\n" +
                "WHERE fg.FILM_ID = ?\n" +
                "ORDER BY fg.GENRE_ID;";
        return new LinkedHashSet<>(jdbcTemplate.query(sqlQuery, this::mapRowToGenre, filmId));
    }

    private Genre mapRowToGenre(ResultSet rs, int rowNum) throws SQLException {
        return new Genre(rs.getLong("id"), rs.getString("name"));
    }

    private Film mapRowToFilm(ResultSet rs, int rowNum) throws SQLException {
        Film film = new Film();
        film.setId(rs.getLong("id"));
        film.setName(rs.getString("name"));
        film.setDescription(rs.getString("description"));
        film.setMpa(getMpaById(rs.getInt("mpa_id")));
        film.setReleaseDate(rs.getDate("release_date").toLocalDate());
        film.setDuration(rs.getLong("duration"));
        return film;
    }

    @Override
    public Mpa getMpaById(int id) {
        String sqlQuery = "SELECT MPA_ID, NAME FROM MPA WHERE MPA_ID = ?";
        return jdbcTemplate.queryForObject(sqlQuery, this::mapRowToMpa, id);
    }

    private Mpa mapRowToMpa(ResultSet rs, int rowNum) throws SQLException {
        return new Mpa(rs.getInt("MPA_ID"), rs.getString("NAME"));
    }


    private Film getFilmFromRepository(long filmId) {
        return Optional.ofNullable(filmRepository.getById(filmId)).orElseThrow(() -> new NotFoundException("Film not found with id: " + filmId));
    }

    private User getUserFromRepository(long userId) {
        return Optional.ofNullable(userRepository.getById(userId)).orElseThrow(() -> new NotFoundException("User not found with id: " + userId));
    }

    @Override
    public List<FilmDto> getPopularFilms(int count, Long genreId, Integer year) {
        List<FilmDto> newListFilm = new ArrayList<>();
        List<Film> filmList = filmRepository.getTopPopular(count, genreId, year);
        for (Film film : filmList) {
            newListFilm.add(FilmMapper.mapToUserDto(film));
        }
        return newListFilm;
    }

    @Override
    public List<FilmDto> getDirectorFilmsSortedBy(long directorId, String sortBy) {
        List<Film> films = filmRepository.getSortedFilmsByDirector(directorId, sortBy);
        return films.stream()
                .map(FilmMapper::mapToUserDto)
                .collect(Collectors.toList());
    }

    private boolean isMpaValid(Film film) {
        Mpa filmMpa = film.getMpa();
        if (mpaRepository.findById(filmMpa.getId()).size() != 1) {
            throw new BadBodyRequestException("These Mpa_Ids" + filmMpa.getId() + " not contains in DATA");
        }
        return true;
    }

    private boolean isGenresValid(Film film) {
        if (film.getGenres() == null) {
            return true;
        }
        List<Long> filmGenreIds = film.getGenres().stream()
                .map(Genre::getId)
                .toList();

        List<Genre> genres = genreRepository.findByIds(filmGenreIds);

        if (genres.size() != filmGenreIds.size()) {
            throw new BadBodyRequestException("These Genre_Ids " + filmGenreIds + " not contains in DATA");
        }
        return true;
    }

}
