package ru.yandex.practicum.filmorate.service.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.dto.UserDto;
import ru.yandex.practicum.filmorate.dao.user.UserRepository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.mapper.UserMapper;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class GeneralUserService implements UserService {

    private final UserRepository userRepository;

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
        Optional<User> savedUSer = (userRepository.getById(userId));
        if (savedUSer.isEmpty()) {
            throw new NotFoundException("User not found with id: " + userId);
        }
        return UserMapper.mapToUserDto(userRepository.update(user));
    }

    @Override
    public UserDto get(long userId) {
        return UserMapper.mapToUserDto((userRepository.getById(userId))
                .orElseThrow(() -> new NotFoundException("User not found with id: " + userId)));
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
        return userRepository.getById(userId)
                .orElseThrow(() -> new NotFoundException("User not found with id: " + userId));
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

    /**
     * Удаление пользователя по Id
     * Сначала пользователь извелкается из базы
     * Потом его данные удаляются
     */
    @Override
    public User deleteUserById(long id) {
        User user = userRepository.getById(id)
                .orElseThrow(() -> new NotFoundException("Пользователь с ID: " + id + " не найден"));
        userRepository.deleteUser(id);
        log.info(String.format("Пользователь с ID: %d удален из базы", id));
        return user;
    }
}
