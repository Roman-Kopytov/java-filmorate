package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    Optional<User> get(long userId);

    User create(User user);


    User update(User user);

    List<User> getUserFriends(User user);

    void addFriend(User user, User friend);

    void deleteFriend(User user, User friend);

    List<User> getAll();


}
