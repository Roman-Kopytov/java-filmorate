package ru.yandex.practicum.filmorate.service.genre;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

@Service
public interface GenreService {
    public Genre getById(long id);

    public List<Genre> getAllGenres();

}
