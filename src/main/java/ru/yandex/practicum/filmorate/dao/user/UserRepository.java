package ru.yandex.practicum.filmorate.dao.user;

import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    Optional<User> getById(long userId);

    List<User> getAll();

    User save(User user);

    User update(User user);

    void deleteUser(long userId);

    List<User> getUserFriends(User user);

    List<User> getCommonFriends(User user, User otherUser);

    void addFriend(User user, User friend);

    void deleteFriend(User user, User friend);

    List<Event> getFeed(long id);

    List<Long> getRecommendation(long id);
}
