package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.constraints.Positive;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dao.dto.FilmDto;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.service.director.DirectorService;

import java.util.List;

@RestController
@RequestMapping("/directors")
@Slf4j
public class DirectorController {
    private final DirectorService service;

    @Autowired
    public DirectorController(DirectorService service) {
        this.service = service;
    }

    @GetMapping
    public List<Director> getAll() {
        log.info("==>GET /directors");
        return service.getAll();
    }

    @GetMapping("/{id}")
    public Director getById(@Positive @PathVariable long id) {
        log.info("==>GET /directors/{}", id);
        Director director = service.getById(id);
        log.info("GET /directors/{} <== {}", id, director);
        return director;
    }

    @PostMapping
    public Director create(@RequestBody Director director) {
        log.info("==>POST /directors {}", director);
        Director directorToReturn = service.create(director);
        log.info("POST /directors <== {}", directorToReturn);
        return directorToReturn;
    }

    @PutMapping
    public Director update(@RequestBody Director director) {
        log.info("==>PUT /directors {}", director);
        Director directorToReturn = service.update(director);
        log.info("PUT /directors <== {}", directorToReturn);
        return directorToReturn;
    }

    @DeleteMapping("/{id}")
    public String deleteDirector(@Positive @PathVariable long id) {
        log.info("==>DELETE /directors/{}", id);
        return service.delete(id);
    }
}