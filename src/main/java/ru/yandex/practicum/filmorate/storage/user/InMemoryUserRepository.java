package ru.yandex.practicum.filmorate.storage.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Slf4j
@Repository
@RequiredArgsConstructor
public class InMemoryUserRepository implements UserRepository {
    private final Map<Long, User> userMap;
    private final Map<Long, Set<User>> userFriends;
    private Long actualId = 0L;

    private Long getNextId() {
        return ++actualId;
    }

    @Override
    public Optional<User> get(long userId) {
        return Optional.ofNullable(userMap.get(userId));
    }

    @Override
    public User create(User user) {
        user.setId(getNextId());
        userMap.put(user.getId(), user);
        return user;
    }

    @Override
    public void delete(User user) {

    }

    @Override
    public User update(User user) {
        if (userMap.get(user.getId()) == null) {
            log.info("User {} not found", user);
            throw new NotFoundException("User" + user.getId() + " not found");
        }
        if (user.getName() == null || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
        userMap.put(user.getId(), user);
        return user;
    }

    @Override
    public ArrayList<User> getUserFriends(User user) {
        long userId = user.getId();
        Set<User> friends = userFriends.getOrDefault(userId, Collections.emptySet());
        if (friends.isEmpty()) {
            return new ArrayList<>();
        }
        return new ArrayList<>(userFriends.get(userId));
    }

    @Override
    public void addFriend(User user, User friend) {
        userFriends.computeIfAbsent(user.getId(), id -> new HashSet<>()).add(friend);
        userFriends.computeIfAbsent(friend.getId(), id -> new HashSet<>()).add(user);
    }

    @Override
    public void deleteFriend(User user, User friend) {
        userFriends.computeIfAbsent(user.getId(), id -> new HashSet<>()).remove(friend);
        userFriends.computeIfAbsent(friend.getId(), id -> new HashSet<>()).remove(user);
    }

    @Override
    public ArrayList<User> getAll() {
        return new ArrayList<>(userMap.values());
    }
}
