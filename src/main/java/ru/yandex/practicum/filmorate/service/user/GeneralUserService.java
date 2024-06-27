package ru.yandex.practicum.filmorate.service.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.dto.EventDto;
import ru.yandex.practicum.filmorate.dao.dto.FilmDto;
import ru.yandex.practicum.filmorate.dao.dto.UserDto;
import ru.yandex.practicum.filmorate.dao.event.JdbcEventRepository;
import ru.yandex.practicum.filmorate.dao.film.FilmRepository;
import ru.yandex.practicum.filmorate.dao.user.UserRepository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.mapper.EventMapper;
import ru.yandex.practicum.filmorate.mapper.FilmMapper;
import ru.yandex.practicum.filmorate.mapper.UserMapper;
import ru.yandex.practicum.filmorate.model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class GeneralUserService implements UserService {

    private final UserRepository userRepository;
    private final FilmRepository filmRepository;
    private final JdbcEventRepository eventRepository;

    private static List<FilmDto> getFilmDtos(final List<Long> listLongFilms, final List<Film> filmList) {
        final List<FilmDto> recommendedFilms = new ArrayList<>();
        for (Long l : listLongFilms) {
            for (Film f : filmList) {
                if (f.getId().equals(l)) {
                    FilmDto filmDto = FilmMapper.mapToFilmDto(f);
                    if (!recommendedFilms.contains(filmDto)) {
                        recommendedFilms.add(filmDto);
                    }
                }
            }
        }
        return recommendedFilms;
    }

    private static List<Long> getLongs(final List<Like> likesList, final List<Long> idFilms, final Long id) {
        final List<Long> similarUser = new ArrayList<>();
        for (Like like : likesList) {
            if (like.getUserId().equals(id)) {
                continue;
            }
            for (Long l : idFilms) {
                if (similarUser.contains(like.getUserId())) {
                    continue;
                }
                if (l.equals(like.getFilmId())) {
                    similarUser.add(like.getUserId());
                }
            }
        }

        final List<Long> listLongFilms = new ArrayList<>();
        for (Like like : likesList) {
            if (like.getUserId().equals(id)) {
                continue;
            }
            for (Long l : similarUser) {
                if (l.equals(like.getUserId())) {
                    if (!idFilms.contains(like.getFilmId())) {
                        listLongFilms.add(like.getFilmId());
                    }
                }
            }
        }
        return listLongFilms;
    }

    @Override
    public UserDto create(final User user) {
        if (user.getName() == null || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
        return UserMapper.mapToUserDto(userRepository.save(user));
    }

    @Override
    public UserDto update(final User user) {
        long userId = user.getId();
        Optional<User> savedUSer = userRepository.getById(userId);
        if (savedUSer.isEmpty()) {
            throw new NotFoundException("User not found with id: " + userId);
        }
        return UserMapper.mapToUserDto(userRepository.update(user));
    }

    @Override
    public UserDto get(final long userId) {
        return UserMapper.mapToUserDto((userRepository.getById(userId))
                .orElseThrow(() -> new NotFoundException("User not found with id: " + userId)));
    }

    @Override
    public void addFriend(final long userId, final long friendId) {
        userRepository.addFriend(getUserFromRepository(userId), getUserFromRepository(friendId));
        eventRepository.saveEvent(userId, friendId, EventType.FRIEND, Operation.ADD);
    }

    @Override
    public void deleteFriend(final long userId, final long friendId) {
        userRepository.deleteFriend(getUserFromRepository(userId), getUserFromRepository(friendId));
        eventRepository.saveEvent(userId, friendId, EventType.FRIEND, Operation.REMOVE);
    }

    private User getUserFromRepository(final long userId) {
        return userRepository.getById(userId)
                .orElseThrow(() -> new NotFoundException("User not found with id: " + userId));
    }

    @Override
    public List<UserDto> getCommonFriends(final long id, final long otherId) {
        List<User> commonFriends = userRepository.getCommonFriends(getUserFromRepository(id), getUserFromRepository(otherId));
        return commonFriends.stream().map(UserMapper::mapToUserDto).collect(Collectors.toList());
    }

    @Override
    public List<UserDto> getUserFriends(final long id) {
        return getFriendsFromRepository(id).stream().map(UserMapper::mapToUserDto).collect(Collectors.toList());
    }

    private List<User> getFriendsFromRepository(final long id) {
        return userRepository.getUserFriends(getUserFromRepository(id));
    }

    @Override
    public List<UserDto> getAll() {
        return userRepository.getAll()
                .stream()
                .map(UserMapper::mapToUserDto)
                .collect(Collectors.toList());
    }

    /**
     * Удаление пользователя по Id
     * Сначала пользователь извелкается из базы
     * Потом его данные удаляются
     */
    @Override
    public User deleteUserById(final long id) {
        User user = userRepository.getById(id)
                .orElseThrow(() -> new NotFoundException("Пользователь с ID: " + id + " не найден"));
        userRepository.deleteUser(id);
        log.info(String.format("Пользователь с ID: %d удален из базы", id));
        return user;
    }

    @Override
    public List<FilmDto> getRecommendations(final long id) {
        final List<Like> likesList = userRepository.getMapUserLikeFilm();
        final List<Film> filmList = filmRepository.getAll();
        final List<Long> idFilms = new ArrayList<>();
        for (Like l : likesList) {
            if (l.getUserId().equals(id)) {
                idFilms.add(l.getFilmId());
            }
        }
        final List<Long> listLongFilms = getLongs(likesList, idFilms, id);
        return getFilmDtos(listLongFilms, filmList);
    }

    @Override
    public List<EventDto> getFeed(final long id) {
        User user = userRepository.getById(id)
                .orElseThrow(() -> new NotFoundException("Пользователь с ID: " + id + " не найден"));
        return userRepository.getFeed(id).stream().map(EventMapper::mapToEventDto).toList();
    }
}
