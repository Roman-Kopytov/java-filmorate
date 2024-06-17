package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.genre.GenreService;

import java.util.List;

@RestController
@RequestMapping("/genres")
@Validated
@Slf4j
@RequiredArgsConstructor
public class GenresHandler {

    private final GenreService genreService;

    @GetMapping("/{id}")
    public Genre getGenreById(@PathVariable("id") long id) {
        log.info("==>GET genres/{}", id);
        return genreService.getById(id);
    }

    @GetMapping()
    public List<Genre> getAllGenres() {
        log.info("==>GET /genres");
        return genreService.getAllGenres();
    }
}
