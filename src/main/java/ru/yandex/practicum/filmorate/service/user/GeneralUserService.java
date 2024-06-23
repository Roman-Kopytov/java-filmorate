package ru.yandex.practicum.filmorate.service.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.dto.FilmDto;
import ru.yandex.practicum.filmorate.dao.dto.UserDto;
import ru.yandex.practicum.filmorate.dao.film.FilmRepository;
import ru.yandex.practicum.filmorate.dao.user.UserRepository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.mapper.FilmMapper;
import ru.yandex.practicum.filmorate.mapper.UserMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Like;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GeneralUserService implements UserService {

    private final UserRepository userRepository;
    private final FilmRepository filmRepository;

    @Override
    public UserDto create(User user) {
        if (user.getName() == null || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
        return UserMapper.mapToUserDto(userRepository.save(user));
    }

    @Override
    public UserDto update(User user) {
        long userId = user.getId();
        Optional<User> savedUSer = userRepository.getById(userId);
        if (savedUSer.isEmpty()) {
            throw new NotFoundException("User not found with id: " + userId);
        }
        return UserMapper.mapToUserDto(userRepository.update(user));
    }

    @Override
    public UserDto get(long userId) {
        return UserMapper.mapToUserDto(userRepository.getById(userId).orElseThrow(()
                -> new NotFoundException("User not found with id: " + userId)));
    }

    @Override
    public void addFriend(long userId, long friendId) {
        userRepository.addFriend(getUserFromRepository(userId), getUserFromRepository(friendId));
    }

    @Override
    public void deleteFriend(long userId, long friendId) {
        userRepository.deleteFriend(getUserFromRepository(userId), getUserFromRepository(friendId));
    }

    private User getUserFromRepository(long userId) {
        return userRepository.getById(userId).orElseThrow(() -> new NotFoundException("User not found with id: " + userId));
    }

    @Override
    public List<UserDto> getCommonFriends(long id, long otherId) {
        List<User> commonFriends = userRepository.getCommonFriends(getUserFromRepository(id), getUserFromRepository(otherId));
        return commonFriends.stream().map(UserMapper::mapToUserDto).collect(Collectors.toList());
    }

    @Override
    public List<UserDto> getUserFriends(long id) {
        return getFriendsFromRepository(id).stream().map(UserMapper::mapToUserDto).collect(Collectors.toList());
    }

    private List<User> getFriendsFromRepository(long id) {
        return userRepository.getUserFriends(getUserFromRepository(id));
    }

    @Override
    public List<UserDto> getAll() {
        return userRepository.getAll()
                .stream()
                .map(UserMapper::mapToUserDto)
                .collect(Collectors.toList());
    }

    private static List<FilmDto> getFilmDtos(List<Long> listLongFilms, List<Film> filmList) {
        final List<FilmDto> recommendedFilms = new ArrayList<>();
        for (Long l : listLongFilms) {
            for (Film f : filmList) {
                if (f.getId().equals(l)) {
                    FilmDto filmDto = FilmMapper.mapToFilmDto(f);
                    if (!recommendedFilms.contains(filmDto))
                        recommendedFilms.add(filmDto);
                }
            }
        }
        return recommendedFilms;
    }

    @Override
    public List<FilmDto> getRecommendations(Long id) {
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

    private static List<Long> getLongs(List<Like> likesList, List<Long> idFilms, Long id) {
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
}
