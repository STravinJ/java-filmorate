package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.validation.Validator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FilmService {

    private final FilmStorage filmStorage;

    public Collection<Film> getAll() {

        return filmStorage.getAll();

    }

    public Film getById(Long filmId) {

        return filmStorage.getById(filmId);

    }

    public Film addFilm(Film film) {

        filmStorage.add(film);
        return film;

    }

    public Film modifyFilm(Film film) {

        filmStorage.modify(film);
        return film;

    }

    public void addLike(Long filmId, Long userId) {

        Validator.userIdValidation(userId);

        Film film = filmStorage.getById(filmId);

        film.addLike(userId);

    }

    public void deleteLike(Long filmId, Long userId) {

        Validator.userIdValidation(userId);

        Film film = filmStorage.getById(filmId);

        film.deleteLike(userId);

    }

    public List<Film> getPopularFilms(int count) {

        List<Film> popularFilms = new ArrayList<>();

        filmStorage.getAll()
                .stream()
                .sorted(new filmLikesCompare())
                .forEach(film -> {
                    if (popularFilms.size() == count) {
                        return;
                    }
                    popularFilms.add(film);
                });

        return popularFilms;

    }

    class filmLikesCompare implements Comparator<Film> {
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
