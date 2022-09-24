package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder
public class User {

    private Long id;
    private String email;
    private String login;
    private String name;
    private LocalDate birthday;
    private Set<Long> friends;

    public void addFriendOfUser(Long friendId) {

        if (friends == null) {
            friends = new HashSet<>();
        }

        friends.add(friendId);
    }

    public void deleteFriendOfUser(Long friendId) {
        friends.remove(friendId);
    }

}
