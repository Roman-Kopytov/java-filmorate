package ru.yandex.practicum.filmorate.dao.genre;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@JdbcTest
@Import({JdbcGenreRepository.class})
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class JdbcGenreRepositoryTest {
    public static final int GENRES_SIZE = 6;
    private final JdbcGenreRepository genreRepository;

    @Test
    void getAllGenres() {
        List<Genre> genresInData = genreRepository.getAll();
        assertEquals(GENRES_SIZE, genresInData.size());
    }

}