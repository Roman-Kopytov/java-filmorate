package ru.yandex.practicum.filmorate.service.user;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

@Service
public interface UserService {

    User create(User user);

    User update(User user);

    User get(long userId);

    void addFriend(long userId, long friendId);

    void deleteFriend(long userId, long friendId);

    List<User> getCommonFriends(long id, long otherId);

    List<User> getUserFriends(long id);

    List<User> getAll();
}
