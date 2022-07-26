package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.validation.Validator;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {

    private final Map<Integer, User> users = new HashMap<>();
    private Integer idUser = 0;

    @GetMapping
    public Collection<User> findAll() {

        log.info("Получение всех пользователей.");
        return users.values();

    }

    @PostMapping
    public User create(@RequestBody User user) {

        Validator.userValidation(user, users);

        user.setId(++idUser);
        users.put(user.getId(), user);
        log.info("Добавление пользователя: {}", user);
        return user;

    }

    @PutMapping
    public User put(@RequestBody User user) {

        Validator.userValidation(user, users);

        users.remove(user.getId());
        users.put(user.getId(), user);
        log.info("Обновленние пользователя: {}", user);
        return user;

    }

}