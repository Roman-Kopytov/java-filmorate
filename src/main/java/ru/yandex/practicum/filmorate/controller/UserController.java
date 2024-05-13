package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.Marker;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.user.UserService;

import java.util.ArrayList;

@RestController
@RequestMapping("/users")
@Validated
@Slf4j
public class UserController {
    @Autowired
    UserService userService;

    @GetMapping
    public ArrayList<User> getAllUsers() {
        return userService.getAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Validated
    public User createUser(@Valid @RequestBody User user) {
        log.info("==>POST /users  {}", user);
        userService.create(user);
        log.info("POST /users <== {}", user);
        return user;
    }

    @PutMapping
    @Validated({Marker.OnUpdate.class})
    public User updateUser(@Valid @RequestBody User user) {
        log.info("==>PUT /users  {}", user);
        userService.update(user);
        log.info("PUT /users <== {}", user);
        return user;
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
    public ArrayList<User> getUserFriends(@PathVariable("id") @Min(0) long id) {
        log.info("==>GET /users/{}/friends", id);
        return userService.getUserFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public ArrayList<User> getCommonFriends(@PathVariable("id") @Min(0) long id,
                                            @PathVariable("otherId") @Min(0) long otherId) {
        log.info("==>GET /users  {}", id);
        return userService.getCommonFriends(id, otherId);
    }

}
