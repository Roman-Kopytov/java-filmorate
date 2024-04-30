package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private Integer actualId = 0;

    @GetMapping
    public ArrayList<User> getAllFilms() {
        return new ArrayList<>(users.values());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public User createUser(@Valid @RequestBody User user) {
        log.info("==>POST /users  {}", user);
        Integer id = getNextId();
        user.setId(id);
        if (user.getName() == null || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
        users.put(id, user);
        log.info("POST /users <== {}", user);
        return user;
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        log.info("==>PUT /users  {}", user);
        try {
            if (users.get(user.getId()) == null) {
                log.info("User {} not found", user);
                throw new RuntimeException();
            }
            users.put(user.getId(), user);
            log.info("PUT /users <== {}", user);

        } catch (RuntimeException e) {
            log.info("Throw RuntimeException", e);
            throw e;
        }
        return user;
    }

    private Integer getNextId() {
        return ++actualId;
    }
}
