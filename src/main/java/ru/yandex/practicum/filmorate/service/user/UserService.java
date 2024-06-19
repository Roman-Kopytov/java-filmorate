package ru.yandex.practicum.filmorate.service.user;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.dto.UserDto;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

@Service
public interface UserService {

    UserDto create(User user);

    UserDto update(User user);

    UserDto get(long userId);

    void addFriend(long userId, long friendId);

    void deleteFriend(long userId, long friendId);

    List<UserDto> getCommonFriends(long id, long otherId);

    List<UserDto> getUserFriends(long id);

    List<UserDto> getAll();

    List<Film> getRecommendations(Long id);
}
