package ru.yandex.practicum.filmorate.service.genre;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

@Service
public interface GenreService {
    Genre getById(long id);

    List<Genre> getAllGenres();

}
