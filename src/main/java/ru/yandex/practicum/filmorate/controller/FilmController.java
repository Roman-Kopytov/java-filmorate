package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.HashMap;

@RestController
@RequestMapping("/films")
@Validated
public class FilmController {

    private final HashMap<Integer, Film> films = new HashMap<>();
    private Integer actualId = 0;
    private static final Logger log = LoggerFactory.getLogger(FilmController.class);

    @GetMapping
    public ArrayList<Film> getAllFilms() {
        return new ArrayList<>(films.values());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Film createFilm(@Valid @RequestBody Film film) {
        log.info("==>POST /films {}", film);
        Integer id = getNextId();
        film.setId(id);
        films.put(id, film);
        log.info("POST /films <== {}", film);
        return film;
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
        log.info("==>PUT /films {}", film);
        try {
            if (films.get(film.getId()) == null) {
                throw new RuntimeException();
            }
            films.put(film.getId(), film);
            log.info("PUT /films <== {}", film);
        } catch (RuntimeException e) {
            log.info("Throw RuntimeException", e);
            throw e;
        }
        return film;
    }

    private Integer getNextId() {
        return ++actualId;
    }


}
