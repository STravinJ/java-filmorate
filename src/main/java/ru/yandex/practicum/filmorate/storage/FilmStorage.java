package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage extends ModelStorage<Film> {

    List<Long> getLikes(Long filmId);

    void saveLikes(Film film);

}
