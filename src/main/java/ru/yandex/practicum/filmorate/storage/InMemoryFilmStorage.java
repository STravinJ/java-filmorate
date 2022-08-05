package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.validation.Validator;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
@RestControllerAdvice
@Slf4j
public class InMemoryFilmStorage implements FilmStorage{

    private final Map<Long, Film> films = new HashMap<>();

    private Long idFilm = 0L;


    @Override
    public void add(Film film) {

        Validator.filmValidation(film, films);

        film.setId(++idFilm);
        films.put(film.getId(), film);

        log.info("Добавление фильма: {}", film);

    }

    @Override
    public Film getById(Long filmId) {

        Validator.filmIdValidation(filmId, films);

        return films.get(filmId);

    }

    @Override
    public void remove(Long filmId) {

        films.remove(filmId);

    }

    @Override
    public Collection<Film> getAll() {

        log.info("Получение всех фильмов.");
        return films.values();

    }

    @Override
    public void modify(Film film) {

        Validator.filmValidation(film, films);

        Film filmBefore = films.get(film.getId());

        film.setUsersLikes(filmBefore.getUsersLikes());

        films.remove(film.getId());
        films.put(film.getId(), film);

        log.info("Обновленние фильма: {}", film);

    }

}
