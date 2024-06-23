package ru.yandex.practicum.filmorate.dao.user;

import ru.yandex.practicum.filmorate.model.Like;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserRepository {
    User getById(long userId);

    List<User> getAll();

    User save(User user);

    User update(User user);

    List<User> getUserFriends(User user);

    List<User> getCommonFriends(User user, User otherUser);

    void addFriend(User user, User friend);

    void deleteFriend(User user, User friend);

    List<Like> getMapUserLikeFilm();
}
