package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.DataNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.Collection;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @GetMapping
    public Collection<User> getAll() {

        return userService.getAll();

    }

    @GetMapping("/{id}")
    public User getById(@PathVariable long id) {
        return userService.getById(id);
    }

    @PostMapping
    public User create(@RequestBody User user) {

        userService.add(user);
        return user;

    }

    @PutMapping
    public User put(@RequestBody User user) {

        userService.modifyUser(user);
        return user;

    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@PathVariable("id") long id,
                          @PathVariable("friendId") long friendId) {

        userService.addFriend(id, friendId);

    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable("id") long id,
                             @PathVariable("friendId") long friendId) {

        userService.deleteFriend(id, friendId);

    }

    @GetMapping("/{id}/friends")
    public Collection<User> getFriends(@PathVariable("id") long id) {

        return userService.getFriends(id);

    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public Collection<User> getCommonFriends(@PathVariable("id") long id,
                                             @PathVariable("otherId") long otherId) {

        return userService.getCommonFriends(id, otherId);

    }

}