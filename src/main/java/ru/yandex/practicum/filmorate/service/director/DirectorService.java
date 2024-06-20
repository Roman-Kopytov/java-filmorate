package ru.yandex.practicum.filmorate.service.director;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.director.JdbcDirectorRepository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Director;

import java.util.List;
import java.util.Optional;


@Service
@Slf4j
public class DirectorService {
    private final JdbcDirectorRepository storage;

    @Autowired
    public DirectorService(JdbcDirectorRepository storage) {
        this.storage = storage;
    }

    public List<Director> getAll() {
        return storage.getAll();
    }

    public Director getById(long id) {
        Optional<Director> director = Optional.ofNullable(storage.getById(id));
        if (director.isEmpty()) {
            throw new NotFoundException("Director not found with id: " + id);
        }

        return storage.getById(id);
    }

    public Director create(Director director) {
        return storage.create(director);
    }

    public Director update(Director director) {
        Director newDirector = storage.update(director);
        return newDirector;
    }

    public void delete(long id) {
        storage.delete(id);
    }
}
