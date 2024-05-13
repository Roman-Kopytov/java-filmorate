package ru.yandex.practicum.filmorate.storage.film;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Repository
@RequiredArgsConstructor
public class InMemoryFilmRepository implements FilmRepository {
    private final Map<Long, Film> filmMap;
    private final Map<Long, Set<Long>> likesMap;
    private Long actualId = 0L;

    @Override
    public Optional<Film> get(long filmId) {
        return Optional.ofNullable(filmMap.get(filmId));
    }

    @Override
    public Film create(Film film) {
        film.setId(getNextId());
        filmMap.put(film.getId(), film);
        return film;
    }

    @Override
    public Film update(Film film) {
        filmMap.put(film.getId(), film);
        return film;
    }

    @Override
    public ArrayList<Film> getAll() {
        return new ArrayList<>(filmMap.values());
    }

    @Override
    public Map<Long, Set<Long>> getLikes() {
        return new HashMap<>(likesMap);
    }

    @Override
    public void addLike(Film film, User user) {
        likesMap.computeIfAbsent(film.getId(), id -> new HashSet<>()).add(user.getId());
    }

    @Override
    public void deleteLike(Film film, User user) {
        likesMap.computeIfAbsent(film.getId(), id -> new HashSet<>()).remove(user.getId());
    }

    private Long getNextId() {
        return ++actualId;
    }
}
