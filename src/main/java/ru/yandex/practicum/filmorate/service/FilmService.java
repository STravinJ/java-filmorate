package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.DataNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.GenreStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import ru.yandex.practicum.filmorate.validation.Validator;

import java.util.*;

@Service
@RequiredArgsConstructor
public class FilmService {

    @Qualifier("FilmDbStorage")
    private final FilmStorage filmStorage;

    @Qualifier("UserDbStorage")
    private final UserStorage userStorage;

    @Qualifier("GenreDbStorage")
    private final GenreStorage genreStorage;

    public Collection<Film> getAll() {

        Collection<Film> films = filmStorage.getAll();

        for (Film film : films) {
            film.setGenres(genreStorage.getGenresByFilm(film.getId()));
            film.setUsersLikes(new HashSet<Long>(filmStorage.getLikes(film.getId())));
        }

        return films;

    }

    public Film getById(Long filmId) {

        Validator.idValidation(filmId);

        Film film = filmStorage.getById(filmId);
        if (film == null) {
            String errorMsg = String.format("Отсутствует фильм с id=%s", filmId);
            throw new DataNotFoundException(errorMsg);
        }

        film.setGenres(genreStorage.getGenresByFilm(filmId));
        film.setUsersLikes(new HashSet<Long>(filmStorage.getLikes(filmId)));

        return film;

    }

    public Film add(Film film) {

        Validator.filmValidation(film);
        filmStorage.add(film);
        return this.getById(film.getId());

    }

    public Film modify(Film film) {

        Validator.filmValidation(film);
        Validator.storageValidation(film, filmStorage);
        filmStorage.modify(film);
        return this.getById(film.getId());

    }

    public void addLike(Long filmId, Long userId) {

        Validator.storageValidation(userId, userStorage);
        Validator.storageValidation(filmId, filmStorage);

        Film film = filmStorage.getById(filmId);

        film.addLike(userId);
        filmStorage.saveLikes(film);

    }

    public void deleteLike(Long filmId, Long userId) {

        Validator.storageValidation(userId, userStorage);
        Validator.storageValidation(filmId, filmStorage);

        Film film = filmStorage.getById(filmId);

        film.deleteLike(userId);
        filmStorage.saveLikes(film);

    }

    public List<Film> getPopularFilms(int count) {

        List<Film> popularFilms = new ArrayList<>();

        Collection<Film> films = this.getAll();

        films.stream()
                .sorted(new FilmLikesCompare())
                .forEach(film -> {
                    if (popularFilms.size() == count) {
                        return;
                    }
                    popularFilms.add(film);
                });

        return popularFilms;

    }

    class FilmLikesCompare implements Comparator<Film> {
        @Override
        public int compare(Film o2, Film o1) {
            if (o1.getUsersLikes() == null
                    && o2.getUsersLikes() == null) {
                return 0;
            } else if (o1.getUsersLikes() == null) {
                return -1;
            } else if (o2.getUsersLikes() == null) {
                return 1;
            } else {
                return o1.getUsersLikes().size() - o2.getUsersLikes().size();
            }
        }
    }

}
