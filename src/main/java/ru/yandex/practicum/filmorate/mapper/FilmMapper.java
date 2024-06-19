package ru.yandex.practicum.filmorate.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.dao.dto.FilmDto;
import ru.yandex.practicum.filmorate.model.Film;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class FilmMapper {
    public static FilmDto mapToUserDto(Film film) {
        FilmDto dto = new FilmDto();
        dto.setId(film.getId());
        dto.setName(film.getName());
        dto.setReleaseDate(film.getReleaseDate());
        dto.setDescription(film.getDescription());
        dto.setDuration(film.getDuration());
        dto.setMpa(film.getMpa());
        dto.setGenres(film.getGenres());
        dto.setDirector(film.getDirector());
        return dto;
    }
}
