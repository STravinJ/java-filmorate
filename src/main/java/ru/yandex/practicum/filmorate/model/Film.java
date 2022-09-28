package ru.yandex.practicum.filmorate.model;

import lombok.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder
@ToString
public class Film extends Model {

    private Long id;
    private String name;
    private String description;
    private LocalDate releaseDate;
    private double duration;
    private Mpa mpa;
    private Set<Long> usersLikes;
    private Set<Genre> genres;

    public void addLike(Long userId) {

        if (usersLikes == null) {
            usersLikes = new HashSet<>();
        }

        usersLikes.add(userId);

    }

    public Set<Long> getLikes() {

        if (usersLikes == null) {
            usersLikes = new HashSet<>();
        }

        return new HashSet<>(usersLikes);

    }

    public void deleteLike(Long userId) {

        if (usersLikes == null) {
            usersLikes = new HashSet<>();
        }

        usersLikes.remove(userId);

    }

}
