package ru.yandex.practicum.filmorate.service;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UserServiceTest {

    private static UserService userService;
    private static User user;
    private static User user2;
    private static User user3;

    @BeforeAll
    static void BeforeAll() {

        UserStorage userStorage = new InMemoryUserStorage();
        userService = new UserService(userStorage);

        user = User.builder()
                .name("user1")
                .login("user1")
                .email("user1@gmail.com")
                .birthday(LocalDate.parse("1895-12-28"))
                .build();

        user2 = User.builder()
                .name("user2")
                .login("user2")
                .email("user2@gmail.com")
                .birthday(LocalDate.parse("1895-12-28"))
                .build();

        user3 = User.builder()
                .name("user3")
                .login("user3")
                .email("user3@gmail.com")
                .birthday(LocalDate.parse("1895-12-28"))
                .build();

        userService.add(user);
        userService.add(user2);
        userService.add(user3);

    }

    @Test
    void addFriend() {

        userService.addFriend(1L, 2L);

        assertEquals(1, user.getFriends().size(), "Количество друзей не совпадает.");
        assertTrue(user.getFriends().contains(2L), "Id друга не совпадает.");

        userService.deleteFriend(1L, 2L);

    }

    @Test
    void deleteFriend() {

        userService.addFriend(1L, 2L);
        userService.deleteFriend(1L, 2L);

        assertEquals(0, user.getFriends().size(), "Количество друзей не совпадает.");

    }

    @Test
    void getFriends() {

        userService.addFriend(1L, 2L);
        userService.addFriend(1L, 3L);
        userService.deleteFriend(1L, 2L);

        assertEquals(1, userService.getFriends(1L).size(), "Количество друзей не совпадает.");
        assertTrue(userService.getFriends(1L).contains(user3), "Id друга не совпадает.");

        userService.deleteFriend(1L, 3L);

    }

    @Test
    void getCommonFriends() {

        userService.addFriend(1L, 2L);
        userService.addFriend(1L, 3L);
        userService.addFriend(3L, 2L);

        assertEquals(1, userService.getCommonFriends(1L, 2L).size(), "Количество друзей не совпадает.");
        assertTrue(userService.getCommonFriends(1L, 2L).contains(user3), "Id друга не совпадает.");

        userService.deleteFriend(1L, 2L);
        userService.deleteFriend(1L, 3L);
        userService.deleteFriend(3L, 2L);

    }

}
