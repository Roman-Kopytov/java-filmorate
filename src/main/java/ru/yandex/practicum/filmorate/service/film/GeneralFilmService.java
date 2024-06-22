package ru.yandex.practicum.filmorate.service.film;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.dto.FilmDto;
import ru.yandex.practicum.filmorate.dao.film.FilmRepository;
import ru.yandex.practicum.filmorate.dao.genre.GenreRepository;
import ru.yandex.practicum.filmorate.dao.mpa.MpaRepository;
import ru.yandex.practicum.filmorate.dao.user.UserRepository;
import ru.yandex.practicum.filmorate.exception.BadBodyRequestException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.mapper.FilmMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GeneralFilmService implements FilmService {

    private final FilmRepository filmRepository;
    private final UserRepository userRepository;
    private final GenreRepository genreRepository;
    private final MpaRepository mpaRepository;

    @Override
    public FilmDto getById(long id) {
        return FilmMapper.mapToFilmDto(getFilmFromRepository(id));

    }

    @Override
    public FilmDto create(Film film) {
        if (isGenresValid(film) && isMpaValid(film)) {
            return FilmMapper.mapToFilmDto(filmRepository.save(film));
        }
        return null;
    }

    @Override
    public FilmDto update(Film film) {
        if (isGenresValid(film) && isMpaValid(film)) {
            long filmId = film.getId();
            getFilmFromRepository(filmId);
            return FilmMapper.mapToFilmDto(filmRepository.update(film));
        }
        return null;
    }

    @Override
    public List<FilmDto> getAll() {
        return filmRepository.getAll().stream()
                .map(FilmMapper::mapToFilmDto)
                .collect(Collectors.toList());
    }

    @Override
    public void addLike(long userId, long filmId) {
        filmRepository.addLike(getFilmFromRepository(filmId), getUserFromRepository(userId));
    }

    @Override
    public void deleteLike(long userId, long filmId) {
        filmRepository.deleteLike(getFilmFromRepository(filmId), getUserFromRepository(userId));
    }

    private Film getFilmFromRepository(long filmId) {
        return Optional.ofNullable(filmRepository.getById(filmId)).orElseThrow(() -> new NotFoundException("Film not found with id: " + filmId));
    }

    private User getUserFromRepository(long userId) {
        return userRepository.getById(userId).orElseThrow(() -> new NotFoundException("User not found with id: " + userId));
    }

    @Override
    public List<Film> getPopularFilms(int count) {
        return filmRepository.getTopPopular(count);
    }


    private boolean isMpaValid(Film film) {
        Mpa filmMpa = film.getMpa();
        if (mpaRepository.findById(filmMpa.getId()).size() != 1) {
            throw new BadBodyRequestException("These Mpa_Ids" + filmMpa.getId() + " not contains in DATA");
        }
        return true;
    }

    private boolean isGenresValid(Film film) {
        if (film.getGenres() == null) {
            return true;
        }
        List<Long> filmGenreIds = film.getGenres().stream()
                .map(g -> g.getId())
                .toList();

        List<Genre> genres = genreRepository.findByIds(filmGenreIds);

        if (genres.size() != filmGenreIds.size()) {
            throw new BadBodyRequestException("These Genre_Ids " + filmGenreIds + " not contains in DATA");
        }
        return true;
    }

}
