package ru.yandex.practicum.filmorate.service.user;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.dao.user.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GeneralUserService implements UserService {

    private final UserRepository userRepository;

    @Override
    public User create(User user) {
        if (user.getName() == null || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
        return userRepository.save(user);
    }

    @Override
    public User update(User user) {
        long userId = user.getId();
        Optional<User> savedUSer = userRepository.getById(userId);
        if (savedUSer.isEmpty()) {
            throw new NotFoundException("User not found with id: " + userId);
        }
        return userRepository.save(user);
    }

    @Override
    public User get(long userId) {
        return userRepository.getById(userId).orElseThrow(() -> new NotFoundException("User not found with id: " + userId));
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
    public List<User> getCommonFriends(long id, long otherId) {
        List<User> userFriends = getFriendsFromRepository(id);
        List<User> otherUserFriends = getFriendsFromRepository(otherId);
        List<User> commonFriends = new ArrayList<>();
        for (User user : userFriends) {
            if (otherUserFriends.contains(user)) {
                commonFriends.add(user);
            }
        }
        return commonFriends;
    }

    @Override
    public List<User> getUserFriends(long id) {
        return getFriendsFromRepository(id);
    }

    private List<User> getFriendsFromRepository(long id) {
        return userRepository.getUserFriends(getUserFromRepository(id));
    }

    @Override
    public List<User> getAll() {
        return userRepository.getAll();
    }
}
