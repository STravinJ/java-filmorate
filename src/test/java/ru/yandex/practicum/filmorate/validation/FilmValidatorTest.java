package ru.yandex.practicum.filmorate.validation;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import ru.yandex.practicum.filmorate.exception.DataNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class FilmValidatorTest {

    Map<Long, Film> films = new HashMap<>();

    private Film createFilm() {

        Mpa mpa = new Mpa(1l, "G");

        Film film = new Film();
        film.setName("film1");
        film.setDescription("decription1");
        film.setDuration(100);
        film.setReleaseDate(LocalDate.parse("1895-12-29"));
        film.setMpa(mpa);

        film.setGenres(new HashSet<>());

        return film;
    }

    @Test
    public void userBadNameTest() throws Exception {

        Film film = createFilm();
        film.setName("");

        final ValidationException exception = assertThrows(
                ValidationException.class,
                new Executable() {
                    @Override
                    public void execute() throws Throwable {

                        Validator.filmValidation(film);

                    }
                }
        );

    }

    @Test
    public void userBadDescriptionTest() throws Exception {

        Film film = createFilm();
        film.setDescription("decript55555555555555555555555555555555555555555555555555555555555555555555555555555555" +
                        "55555555555555555555555555555555555555555555555555555555555555555555555555555555555555555555" +
                        "55555555555555555555555555555555555555555555555555555555555555555555555555555555555555555555");

        final ValidationException exception = assertThrows(
                ValidationException.class,
                new Executable() {
                    @Override
                    public void execute() throws Throwable {

                        Validator.filmValidation(film);

                    }
                }
        );

    }

    @Test
    public void userBadDurationTest() throws Exception {

        Film film = createFilm();
        film.setDuration(-100);

        final ValidationException exception = assertThrows(
                ValidationException.class,
                new Executable() {
                    @Override
                    public void execute() throws Throwable {

                        Validator.filmValidation(film);

                    }
                }
        );

    }

    @Test
    public void userBadReleaseDateTest() throws Exception {

        Film film = createFilm();
        film.setReleaseDate(LocalDate.parse("1895-12-25"));

        final ValidationException exception = assertThrows(
                ValidationException.class,
                new Executable() {
                    @Override
                    public void execute() throws Throwable {

                        Validator.filmValidation(film);

                    }
                }
        );

    }

    @Test
    public void filmWrongId() throws Exception {

        Film film = createFilm();
        film.setId(-1L);

        final DataNotFoundException exception = assertThrows(
                DataNotFoundException.class,
                new Executable() {
                    @Override
                    public void execute() throws Throwable {

                        Validator.idValidation(film.getId());

                    }
                }
        );

    }

}
