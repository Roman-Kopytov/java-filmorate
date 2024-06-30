/*

package ru.yandex.practicum.filmorate.dao.film;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.time.LocalDate;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@JdbcTest
@Import({JdbcFilmRepository.class})
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class JdbcFilmRepositoryTest {
    private final JdbcFilmRepository filmRepository;

    static Film getTestFilm(int filmId) {
        LinkedHashSet<Genre> genres1 = new LinkedHashSet<>(List.of(new Genre(1L, "Комедия"), new Genre(2L, "Драма")));
        Film film1 = new Film(1L, "TJjtJuGS8dUeAzm", "HZOE3ct3plkt3m4ip6dN4EMzqop93SdO5QdJD16uzdhDIgUaQl",
                LocalDate.of(1962, 1, 31), 156L, genres1, new Mpa(3, "PG-13"), (HashSet<Director>) Set.of(new Director(1L)));

        LinkedHashSet<Genre> genres2 = new LinkedHashSet<>(List.of(new Genre(3L, "Мультфильм")));
        Film film2 = new Film(2L, "8B8qgTBRtGKBdJN", "wrVuIIL79f228O2tecGsMdMVbltg1xKpz5qLz86LVHIOv9xJq1",
                LocalDate.of(1962, 3, 6), 65L, genres2, new Mpa(4, "R"), (HashSet<Director>) Set.of(new Director(2L)));

        LinkedHashSet<Genre> genres3 = new LinkedHashSet<>(List.of(new Genre(4L, "Триллер")));
        Film film3 = new Film(3L, "uUk6L30WM5jNBHc", "cB22DWO2euD7py3KEnxpmqcBOh2sJZAOkHJP1pxgPlvbnuxEVW",
                LocalDate.of(1997, 03, 04), 118L, genres3, new Mpa(1, "G"), (HashSet<Director>) Set.of(new Director(3L)));
        List<Film> films = new LinkedList<>(List.of(film1, film2, film3));
        return films.get(filmId - 1);
    }

    static Film getFilmForUpdate() {
        LinkedHashSet<Genre> genres1 = new LinkedHashSet<>(List.of(new Genre(2L, "Драма")));
        Film film = new Film(1L, "TJjtJuGS8dUeAzm", "HZOE3ct3plkt3m4ip6dN4EMzqop93SdO5QdJD16uzdhDIgUaQl",
                LocalDate.of(1962, 1, 31), 156L, genres1, new Mpa(3, "PG-13"), (HashSet<Director>) Set.of(new Director(1L)));
        return film;
    }

    @Test
    void testGetById() {
        Film filmInData = filmRepository.getById(1);
        assertThat(filmInData)
                .usingRecursiveComparison()
                .isEqualTo(getTestFilm(1));
    }

    @Test
    void testSave() {
        Film film = getFilmForUpdate();
        film.setId(null);
        Film updatedFilm = filmRepository.save(getFilmForUpdate());
        Film filmInData = filmRepository.getById(updatedFilm.getId());
        assertThat(filmInData)
                .usingRecursiveComparison()
                .ignoringExpectedNullFields()
                .isEqualTo(updatedFilm);
    }

    @Test
    void testUpdate() {
        Film filmInData = filmRepository.update(getFilmForUpdate());
        assertThat(filmInData)
                .usingRecursiveComparison()
                .isEqualTo(getFilmForUpdate());
    }

    @Test
    void testGetAll() {
        List<Film> filmsInData = filmRepository.getAll();
        assertEquals(3, filmsInData.size());
        assertThat(filmsInData)
                .contains(getTestFilm(1), getTestFilm(2), getTestFilm(3));
    }

    @Test
    void addLike() {
    }

    @Test
    void deleteLike() {
    }

    @Test
    void getTopPopular() {
        List<Film> filmsInData = filmRepository.getTopPopular(10, null, null);
        assertEquals(3, filmsInData.size());
        LinkedList<Film> expectedTop = new LinkedList<>(List.of(getTestFilm(2), getTestFilm(1), getTestFilm(3)));
        assertEquals(expectedTop, filmsInData);
    }
}

 */