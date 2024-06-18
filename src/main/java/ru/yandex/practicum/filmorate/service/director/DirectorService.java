package ru.yandex.practicum.filmorate.service.director;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.director.JdbcDirectorRepository;
import ru.yandex.practicum.filmorate.model.Director;

import java.util.List;


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
        return storage.getById(id);
    }

    public Director create(Director director) {
        return storage.create(director);
    }

    public Director update(Director director) {
        return storage.update(director);
    }

    public void delete(long id) {
        storage.delete(id);
    }
}
