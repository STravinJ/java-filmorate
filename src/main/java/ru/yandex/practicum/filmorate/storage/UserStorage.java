package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage extends ModelStorage<User> {

    void addFriend(Long userId, Long friendId);

    void modifyFriend(Long userId, Long friendId, boolean confirmed, Long userIdBefore, Long friendIdBefore);

    void removeFriend(Long userId, Long friendId);

    boolean getFriends(Long userId, Long friendId, Boolean isConfirmed);

    List<User> getFriends(Long userId);

}
