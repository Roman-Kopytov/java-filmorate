package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.HashMap;

@RestController
@RequestMapping("/films")
@Validated
public class FilmController {

    private final HashMap<Integer, Film> films = new HashMap<>();
    private Integer actualId = 0;

    @GetMapping
    public ArrayList<Film> getAllFilms() {
        return new ArrayList<>(films.values());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Film createFilm(@Valid @RequestBody Film film) {
        Integer id = getNextId();
        film.setId(id);
        films.put(id, film);
        return film;
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
        if (films.get(film.getId()) == null){
            throw new RuntimeException();
        }
        films.put(film.getId(), film);
        return film;
    }

//    @ExceptionHandler(RuntimeException.class)
//    public ResponseStatusException handleException(RuntimeException e){
//        return new ResponseStatusException(HttpStatus.BAD_REQUEST);
//    }

    private Integer getNextId() {
        return ++actualId;
    }


}
