package ru.yandex.practicum.filmorate.service.director;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.director.JdbcDirectorRepository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Director;

import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class DirectorService {
    private final JdbcDirectorRepository storage;

    public List<Director> getAll() {
        return storage.getAll();
    }

    public Director getById(long id) {
        return Optional.ofNullable(storage.getById(id)).orElseThrow(() -> new NotFoundException("Director not found with id: " + id));
    }

    public Director create(Director director) {
        return storage.create(director);
    }

    public Director update(Director director) {
        Optional.ofNullable(storage.getById(director.getId())).orElseThrow(() -> new NotFoundException("Director not found with id: " + director.getId()));
        Director newDirector = storage.update(director);
        return newDirector;
    }

    public void delete(long id) {
        Optional.ofNullable(storage.getById(id)).orElseThrow(() -> new NotFoundException("Director not found with id: " + id));
        storage.delete(id);
    }
}
