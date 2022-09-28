package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.DataNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import ru.yandex.practicum.filmorate.validation.Validator;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    @Qualifier("UserDbStorage")
    private final UserStorage userStorage;

    public User add(User user) {

        Validator.userValidation(user);
        userStorage.add(user);
        return this.getById(user.getId());

    }

    public void remove(Long userId) {

        Validator.storageValidation(userId, userStorage);
        userStorage.remove(userId);

    }

    public User getById(Long userId) {

        Validator.idValidation(userId);

        User user = userStorage.getById(userId);
        if (user == null) {
            String errorMsg = String.format("Отсутствует пользователь с id=%s", userId);
            throw new DataNotFoundException(errorMsg);
        }

        return user;

    }

    public Collection<User> getAll() {

        return userStorage.getAll();
    }

    public User modifyUser(User user) {

        Validator.userValidation(user);
        Validator.storageValidation(user, userStorage);

        userStorage.modify(user);
        return this.getById(user.getId());

    }

    public void addFriend(Long userId, Long friendId) {

        Validator.storageValidation(userId, userStorage);
        Validator.storageValidation(friendId, userStorage);

        User user = userStorage.getById(userId);
        User friend = userStorage.getById(friendId);

        user.addFriendOfUser(friendId);

        if (userStorage.getFriends(friendId, userId, false)) {
            userStorage.modifyFriend(friendId, userId, true, friendId, userId);
        } else if (!userStorage.getFriends(userId, friendId, null)) {
            userStorage.addFriend(userId, friendId);
        }

    }

    public void deleteFriend(Long userId, Long friendId) {

        Validator.storageValidation(userId, userStorage);
        Validator.storageValidation(friendId, userStorage);

        User user = userStorage.getById(userId);

        user.deleteFriendOfUser(friendId);

        if (userStorage.getFriends(userId, friendId, false)) {
            userStorage.removeFriend(userId, friendId);
        } else if (userStorage.getFriends(userId, friendId, true)) {
            userStorage.modifyFriend(friendId, userId, false, userId, friendId);
        } else if (userStorage.getFriends(friendId, userId, true)) {
            userStorage.modifyFriend(friendId, userId, false, friendId, userId);
        }

    }

    public List<User> getFriends(Long userId) {

        Validator.storageValidation(userId, userStorage);

        return userStorage.getFriends(userId);

    }

    public List<User> getCommonFriends(Long userId, Long friendId) {

        List<User> sameFriends = new ArrayList<>();

        User user = userStorage.getById(userId);
        User friend = userStorage.getById(friendId);

        if (user.getFriends() == null || friend.getFriends() == null) {
            return sameFriends;
        }

        Set<Long> sameFriendsId = findCommonElements(user.getFriends(), friend.getFriends());

        for (Long sameFriendId : sameFriendsId) {
            sameFriends.add(getById(sameFriendId));
        }

        return sameFriends;

    }

    private static <T> Set<T> findCommonElements(Set<T> first, Set<T> second) {
        Set<T> collection = new HashSet<>(second);
        return first.stream().filter(collection::contains).collect(Collectors.toSet());
    }

}
