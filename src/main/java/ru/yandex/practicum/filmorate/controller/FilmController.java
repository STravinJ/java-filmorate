package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NoModelException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.Collection;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/films")
public class FilmController {

    private final FilmService filmService;

    @GetMapping
    public Collection<Film> getAll() {

        return filmService.getAll();

    }

    @GetMapping("/{id}")
    public Film getById(@PathVariable long id) {
        return filmService.getById(id);
    }

    @PostMapping
    public Film create(@RequestBody Film film) {

        filmService.addFilm(film);
        return film;

    }

    @PutMapping
    public Film put(@RequestBody Film film) {

        filmService.modifyFilm(film);
        return film;

    }

    @PutMapping("/{id}/like/{userId}")
    public void addLike (@PathVariable("id")  long filmId,
                         @PathVariable("userId")  long userId) {

        filmService.addLike(filmId, userId);

    }

    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLike (@PathVariable("id")  long filmId,
                            @PathVariable("userId")  long userId) {

        filmService.deleteLike(filmId, userId);

    }

    @GetMapping("/popular")
    public Collection<Film> popularFilms (@RequestParam(required = false) Integer count) {

        if (count == null) {
            count = 10;
        }
        if (count <= 0) {
            throw new ValidationException("Параметр count имеет отрицательное значение.");
        }

        return filmService.getPopularFilms(count);

    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleIllegalArgument(final ValidationException e) {
        return Map.of(
                "error", "Ошибка валидации.",
                "errorMessage", e.getMessage()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handleModel(final NoModelException e) {
        return Map.of(
                "error", "Объект не найден.",
                "errorMessage", e.getMessage()
        );
    }

}
