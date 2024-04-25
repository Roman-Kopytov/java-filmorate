package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;

@RestController
@RequestMapping("/users")
@Validated
public class UserController {
    private final HashMap<Integer, User> users = new HashMap<>();

    private Integer actualId = 0;

    @GetMapping
    public ArrayList<User> getAllFilms() {
        return new ArrayList<>(users.values());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public User createUser(@Valid @RequestBody User user) {
        Integer id = getNextId();
        user.setId(id);
        if (user.getName() == null || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
        users.put(id, user);
        return user;
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        if (users.get(user.getId()) == null){
            throw new RuntimeException();
        }
            users.put(user.getId(), user);
        return user;
    }

    private Integer getNextId() {
        return ++actualId;
    }
}
