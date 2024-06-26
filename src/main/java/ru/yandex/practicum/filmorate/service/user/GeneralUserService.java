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
import ru.yandex.practicum.filmorate.model.EventType;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Operation;
import ru.yandex.practicum.filmorate.model.User;

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

    @Override
    public User deleteUserById(final long id) {
        User user = userRepository.getById(id)
                .orElseThrow(() -> new NotFoundException("Пользователь с ID: " + id + " не найден"));
        userRepository.deleteUser(id);
        log.info(String.format("Пользователь с ID: %d удален из базы", id));
        return user;
    }

    @Override
    public List<FilmDto> getRecommendations(final long userId) {
        List<Long> userIdList = userRepository.getRecommendation(userId);
        if (userIdList.isEmpty()) {
            return new ArrayList<>();
        } else {
            List<Film> filmList = filmRepository.getRecommendation(userIdList, userId);
            return filmList.stream()
                    .map(film -> FilmMapper.mapToFilmDto(film))
                    .toList();
        }
    }

    @Override
    public List<EventDto> getFeed(final long id) {
        userRepository.getById(id)
                .orElseThrow(() -> new NotFoundException("Пользователь с ID: " + id + " не найден"));
        return userRepository.getFeed(id).stream().map(EventMapper::mapToEventDto).toList();
    }
}
