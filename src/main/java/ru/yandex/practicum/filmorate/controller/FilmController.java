package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.validation.Validator;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {

    private final Map<Integer, Film> films = new HashMap<>();
    private Integer idUser = 0;

    @GetMapping
    public Collection<Film> findAll() {

        log.info("Получение всех фильмов.");
        return films.values();

    }

    @PostMapping
    public Film create(@RequestBody Film film) {

        Validator.filmValidation(film, films);

        film.setId(++idUser);
        films.put(film.getId(), film);
        log.info("Добавление фильма: {}", film);
        return film;

    }

    @PutMapping
    public Film put(@RequestBody Film film) {

        Validator.filmValidation(film, films);

        films.remove(film.getId());
        films.put(film.getId(), film);
        log.info("Обновленние фильма: {}", film);
        return film;

    }

}
