package ru.yandex.practicum.filmorate.service.user;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserRepository;

import java.util.ArrayList;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GeneralUserService implements UserService {
    @Autowired
    private final UserRepository userRepository;

    @Override
    public User create(User user) {
        if (user.getName() == null || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
        return userRepository.create(user);
    }

    @Override
    public User update(User user) {
        long userId = user.getId();
        Optional<User> savedUSer = userRepository.get(userId);
        if (savedUSer.isEmpty()) {
            throw new NotFoundException("User not found with id: " + userId);
        }
        return userRepository.update(user);
    }

    @Override
    public User get(long userId) {
        return userRepository.get(userId).orElseThrow(() -> new NotFoundException("User not found with id: " + userId));
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
        return userRepository.get(userId).orElseThrow(() -> new NotFoundException("User not found with id: " + userId));
    }

    @Override
    public ArrayList<User> getCommonFriends(long id, long otherId) {
        ArrayList<User> userFriends = getFriendsFromRepository(id);
        ArrayList<User> otherUserFriends = getFriendsFromRepository(otherId);
        ArrayList<User> commonFriends = new ArrayList<>();
        for (User user : userFriends) {
            if (otherUserFriends.contains(user)) {
                commonFriends.add(user);
            }
        }
        return commonFriends;
    }

    @Override
    public ArrayList<User> getUserFriends(long id) {
        return getFriendsFromRepository(id);
    }

    private ArrayList<User> getFriendsFromRepository(long id) {
        return userRepository.getUserFriends(getUserFromRepository(id));
    }

    @Override
    public ArrayList<User> getAll() {
        return userRepository.getAll();
    }
}
