package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.dao.UserDbStorage;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class UserDbStorageTest {
    private final UserDbStorage userStorage;

    private User createUser1() {

        User user = new User();
        user.setName("user1");
        user.setLogin("user1");
        user.setEmail("testtest.ru");
        user.setBirthday(LocalDate.parse("1895-12-28"));

        return user;
    }

    private User createUser2() {

        User user = new User();
        user.setName("user2");
        user.setLogin("user2");
        user.setEmail("testtest2.ru");
        user.setBirthday(LocalDate.parse("1895-12-28"));

        return user;

    }

    @Test
    void getById() {

        User user1 = createUser1();
        userStorage.add(user1);
        User actUser = userStorage.getById(user1.getId());
        assertEquals(user1.getId(), actUser.getId());
        assertEquals(user1.getName(), actUser.getName());

    }

    @Test
    void getAll() {

        User user1 = createUser1();
        userStorage.add(user1);
        User user2 = createUser2();
        userStorage.add(user2);
        Collection<User> users = List.of(user1, user2);

        Collection<User> storageUsers = userStorage.getAll();
        assertEquals(users, storageUsers);
        assertEquals(2, storageUsers.size());

    }

    @Test
    void add() {

        User user1 = createUser1();
        userStorage.add(user1);
        User storageUser = userStorage.getById(user1.getId());
        assertEquals(user1.getId(), storageUser.getId());
        assertEquals(user1.getName(),storageUser.getName());
        assertEquals(user1.getEmail(), storageUser.getEmail());
        assertEquals(user1.getLogin(), storageUser.getLogin());
        assertEquals(user1.getBirthday(), storageUser.getBirthday());

    }

    @Test
    void modify() {

        User user1 = createUser1();
        userStorage.add(user1);
        user1.setName("Super User");

        userStorage.modify(user1);
        User storageUser = userStorage.getById(user1.getId());

        assertEquals(user1.getId(), storageUser.getId());
        assertEquals(user1.getName(), storageUser.getName());

    }

    @Test
    void addFriend() {

        User user1 = createUser1();
        userStorage.add(user1);
        User user2 = createUser2();
        userStorage.add(user2);

        List<User> friends1 = userStorage.getFriends(user1.getId());
        assertTrue(friends1.isEmpty());
        userStorage.addFriend(user1.getId(), user2.getId());
        List<User> storageFriends = userStorage.getFriends(user1.getId());
        assertEquals(user2.getId(), storageFriends.get(0).getId());

        List<User> friends2 = userStorage.getFriends(user2.getId());
        assertTrue(friends2.isEmpty());
        userStorage.addFriend(user2.getId(), user1.getId());
        List<User> storageFriends2 = userStorage.getFriends(user2.getId());
        assertEquals(user1.getId(), storageFriends2.get(0).getId());

    }

    @Test
    void removeFriend() {

        User user1 = createUser1();
        userStorage.add(user1);
        User user2 = createUser2();
        userStorage.add(user2);

        userStorage.addFriend(user1.getId(), user2.getId());
        userStorage.addFriend(user2.getId(), user1.getId());
        assertFalse(userStorage.getFriends(user1.getId()).isEmpty());
        assertFalse(userStorage.getFriends(user2.getId()).isEmpty());

        userStorage.removeFriend(user1.getId(), user2.getId());
        assertTrue(userStorage.getFriends(user1.getId()).isEmpty());
        assertFalse(userStorage.getFriends(user2.getId()).isEmpty());

        userStorage.removeFriend(user2.getId(), user1.getId());
        assertTrue(userStorage.getFriends(user1.getId()).isEmpty());
        assertTrue(userStorage.getFriends(user2.getId()).isEmpty());

    }

}
