package ru.yandex.practicum.filmorate.service.user;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;

@Service
public interface UserService {

    User create(User user);

    User update(User user);

    User get(long userId);

    void addFriend(long userId, long friendId);

    void deleteFriend(long userId, long friendId);

    ArrayList<User> getCommonFriends(long id, long otherId);

    ArrayList<User> getUserFriends(long id);

    ArrayList<User> getAll();
}
