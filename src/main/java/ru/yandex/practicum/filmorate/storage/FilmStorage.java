package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;

public interface FilmStorage {

    void add(Film film);

    void remove(Long filmId);

    Collection<Film> getAll();

    void modify(Film film);

    Film getById(Long filmId);

}
