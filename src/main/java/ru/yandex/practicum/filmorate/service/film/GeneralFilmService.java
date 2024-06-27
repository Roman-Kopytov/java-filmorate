package ru.yandex.practicum.filmorate.service.film;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.director.DirectorRepository;
import ru.yandex.practicum.filmorate.dao.dto.FilmDto;
import ru.yandex.practicum.filmorate.dao.event.JdbcEventRepository;
import ru.yandex.practicum.filmorate.dao.film.FilmRepository;
import ru.yandex.practicum.filmorate.dao.genre.GenreRepository;
import ru.yandex.practicum.filmorate.dao.mpa.MpaRepository;
import ru.yandex.practicum.filmorate.dao.user.UserRepository;
import ru.yandex.practicum.filmorate.exception.BadBodyRequestException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.mapper.FilmMapper;
import ru.yandex.practicum.filmorate.model.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Slf4j
@Service
@RequiredArgsConstructor
public class GeneralFilmService implements FilmService {

    private final FilmRepository filmRepository;
    private final UserRepository userRepository;
    private final GenreRepository genreRepository;
    private final DirectorRepository directorRepository;
    private final MpaRepository mpaRepository;
    private final JdbcEventRepository eventRepository;

    @Override
    public FilmDto getById(long id) {
        return FilmMapper.mapToFilmDto(getFilmFromRepository(id));
    }

    @Override
    public List<FilmDto> searchBy(String query, String by) {
        return filmRepository.searchBy(query, by).stream()
                .map(FilmMapper::mapToFilmDto)
                .collect(Collectors.toList());
    }

    @Override
    public FilmDto deleteFilmById(long id) {
        Film film = filmRepository.getById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Фильм с ID %d не найден", id)));
        filmRepository.deleteFilmById(id);
        log.info(String.format("Фильм с ID %d был успешно удален", id));
        return FilmMapper.mapToFilmDto(film);
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
            filmRepository.getById(film.getId())
                    .orElseThrow(() -> new NotFoundException(String.format("Фильс с ID %d не найден", film.getId())));
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
        eventRepository.saveEvent(userId, filmId, EventType.LIKE, Operation.ADD);
    }

    @Override
    public void deleteLike(long userId, long filmId) {
        filmRepository.deleteLike(getFilmFromRepository(filmId), getUserFromRepository(userId));
        eventRepository.saveEvent(userId, filmId, EventType.LIKE, Operation.REMOVE);
    }


    @Override
    public List<FilmDto> getCommonFilms(long userId, long friendId) {
        return filmRepository.getCommonFilms(userId, friendId).stream()
                .map(FilmMapper::mapToFilmDto)
                .collect(Collectors.toList());
    }

    private Film getFilmFromRepository(long filmId) {
        return filmRepository.getById(filmId)
                .orElseThrow(() -> new NotFoundException("Film not found with id: " + filmId));
    }

    private User getUserFromRepository(long userId) {
        return userRepository.getById(userId).orElseThrow(() -> new NotFoundException("User not found with id: " + userId));
    }

    @Override
    public List<FilmDto> getPopularFilms(int count, Long genreId, Integer year) {
        List<Film> filmList = filmRepository.getTopPopular(count, genreId, year);
        List<FilmDto> newListFilm = filmList.stream().map(film -> FilmMapper.mapToFilmDto(film)).toList();
        return newListFilm;
    }

    @Override
    public List<FilmDto> getDirectorFilmsSortedBy(long directorId, SortedBy sortBy) {
        Optional<Director> director = Optional.ofNullable(directorRepository.getById(directorId));
        if (director.isEmpty()) {
            throw new NotFoundException("Director not found with ID:" + directorId);
        } else {
            List<Film> films = filmRepository.getSortedFilmsByDirector(directorId, sortBy.toString());
            return films.stream()
                    .map(FilmMapper::mapToFilmDto)
                    .collect(Collectors.toList());
        }
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
