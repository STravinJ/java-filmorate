package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder
public class Film {

    private Long id;
    private String name;
    private String description;
    private LocalDate releaseDate;
    private double duration;
    private Set<Long> usersLikes;

    public void addLike(Long userId) {

        if (usersLikes == null) {
            usersLikes = new HashSet<>();
        }

        usersLikes.add(userId);

    }

    public void deleteLike(Long userId) {
        usersLikes.remove(userId);
    }

}
