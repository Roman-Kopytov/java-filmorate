package ru.yandex.practicum.filmorate.service.genre;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.genre.GenreRepository;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

@RequiredArgsConstructor
@Service
public class GeneralGenreService implements GenreService {
    final private GenreRepository genreRepository;

    @Override
    public Genre getById(long id) {
        return genreRepository.getById(id);
    }

    @Override
    public List<Genre> getAllGenres() {
        return genreRepository.getAll();
    }
}
