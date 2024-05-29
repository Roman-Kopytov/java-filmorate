package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.Marker.OnUpdate;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.film.FilmService;

import java.util.ArrayList;
import java.util.Collection;

@RestController
@RequestMapping("/films")
@Validated
@Slf4j
public class  FilmController {

    @Autowired
    FilmService filmService;

    @GetMapping
    public ArrayList<Film> getAllFilms() {
        return filmService.getAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Validated
    public Film create(@Valid @RequestBody Film film) {
        log.info("==>POST /films {}", film);
        Film createdFilm = filmService.create(film);
        log.info("POST /films <== {}", createdFilm);
        return createdFilm;
    }

    @PutMapping
    @Validated({OnUpdate.class})
    public Film update(@Valid @RequestBody Film film) {
        log.info("==>PUT /films {}", film);
        Film updatedFilm = filmService.update(film);
        log.info("PUT /films <== {}", updatedFilm);
        return updatedFilm;
    }

    @GetMapping("/popular")
    public Collection<Film> getPopularFilms(@RequestParam(name = "count", defaultValue = "10") int count) {
        log.info("==>GET /popular?count={}", count);
        return filmService.getPopularFilms(count);
    }

    @PutMapping("/{filmId}/like/{userId}")
    public void addLike(@PathVariable("filmId") @Min(0) long filmId, @PathVariable("userId") @Min(0) long userId) {
        log.info("==>PUT /films/{}/like/{}", filmId, userId);
        filmService.addLike(userId, filmId);
    }

    @DeleteMapping("/{filmId}/like/{userId}")
    public void deleteLike(@PathVariable("filmId") @Min(0) long filmId, @PathVariable("userId") @Min(0) long userId) {
        log.info("==>DELETE /films/{}/like/{}", filmId, userId);
        filmService.deleteLike(userId, filmId);
    }


}
