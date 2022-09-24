package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserStorage userStorage;

    public void add(User user) {

        userStorage.add(user);

    }

    public void remove(Long userId) {

        userStorage.remove(userId);

    }

    public User getById(Long userId) {

        return userStorage.getById(userId);

    }

    public Collection<User> getAll() {

        return userStorage.getAll();
    }

    public void modifyUser(User user) {

        userStorage.modifyUser(user);

    }

    public void addFriend(Long userId, Long friendId) {

        User user = userStorage.getById(userId);
        User friend = userStorage.getById(friendId);

        user.addFriendOfUser(friendId);
        friend.addFriendOfUser(userId);

    }

    public void deleteFriend(Long userId, Long friendId) {

        User user = userStorage.getById(userId);
        User friend = userStorage.getById(friendId);

        user.deleteFriendOfUser(friendId);
        friend.deleteFriendOfUser(userId);

    }

    public List<User> getFriends(Long userId) {

        List<User> friends = new ArrayList<>();

        User user = userStorage.getById(userId);

        user.getFriends().forEach((e) -> { friends.add(userStorage.getById(e)); });

        return friends;

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
