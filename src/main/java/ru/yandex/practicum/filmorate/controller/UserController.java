package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dao.dto.UserDto;
import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.model.Marker.Update;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.user.UserService;

import java.util.List;

@RestController
@RequestMapping("/users")
@Validated
@Slf4j
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public List<UserDto> getAllUsers() {
        return userService.getAll();
    }

    @GetMapping("/{id}")
    public UserDto getUserById(@PathVariable int id) {
        return userService.get(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Validated
    public UserDto createUser(@Valid @RequestBody User user) {
        log.info("==>POST /users  {}", user);
        UserDto newUser = userService.create(user);
        log.info("POST /users <== {}", newUser);
        return newUser;
    }

    @PutMapping
    @Validated({Update.class})
    public UserDto updateUser(@Valid @RequestBody User user) {
        log.info("==>PUT /users  {}", user);
        UserDto updatedUser = userService.update(user);
        log.info("PUT /users <== {}", updatedUser);
        return updatedUser;
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@PathVariable(name = "id") long id, @PathVariable(name = "friendId") long friendId) {
        log.info("==>PUT /users/{}/friends/{}", id, friendId);
        userService.addFriend(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable("id") @Min(0) long id, @PathVariable("friendId") @Min(0) long friendId) {
        log.info("==>DELETE /users/{}/friends/{}", id, friendId);
        userService.deleteFriend(id, friendId);
    }

    @GetMapping("/{id}/friends")
    public List<UserDto> getUserFriends(@PathVariable("id") @Min(0) long id) {
        log.info("==>GET /users/{}/friends", id);
        return userService.getUserFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<UserDto> getCommonFriends(@PathVariable("id") @Min(0) long id,
                                          @PathVariable("otherId") @Min(0) long otherId) {
        log.info("==>GET /users  {}", id);
        return userService.getCommonFriends(id, otherId);
    }

    @GetMapping("/{id}/feed")
    public List<Event> getFeed(@PathVariable long id) {
        log.info("==>GET /users/{}/feed", id);
        return userService.getFeed(id);
    }

}
