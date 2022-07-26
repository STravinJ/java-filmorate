package ru.yandex.practicum.filmorate.validation;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class FilmValidatorTest {

    Map<Integer, Film> films = new HashMap<>();

    @Test
    public void userBadNameTest() throws Exception {

        Film film = Film.builder()
                .name("")
                .description("decription1")
                .duration(100)
                .releaseDate(LocalDate.parse("1895-12-29"))
                .build();

        final ValidationException exception = assertThrows(
                ValidationException.class,
                new Executable() {
                    @Override
                    public void execute() throws Throwable {

                        Validator.filmValidation(film, films);

                    }
                }
        );

    }

    @Test
    public void userBadDescriptionTest() throws Exception {

        Film film = Film.builder()
                .name("film1")
                .description("decript55555555555555555555555555555555555555555555555555555555555555555555555555555555" +
                        "55555555555555555555555555555555555555555555555555555555555555555555555555555555555555555555" +
                        "55555555555555555555555555555555555555555555555555555555555555555555555555555555555555555555")
                .duration(100)
                .releaseDate(LocalDate.parse("1895-12-29"))
                .build();

        final ValidationException exception = assertThrows(
                ValidationException.class,
                new Executable() {
                    @Override
                    public void execute() throws Throwable {

                        Validator.filmValidation(film, films);

                    }
                }
        );

    }

    @Test
    public void userBadDurationTest() throws Exception {

        Film film = Film.builder()
                .name("film1")
                .description("decription1")
                .duration(-100)
                .releaseDate(LocalDate.parse("1895-12-29"))
                .build();

        final ValidationException exception = assertThrows(
                ValidationException.class,
                new Executable() {
                    @Override
                    public void execute() throws Throwable {

                        Validator.filmValidation(film, films);

                    }
                }
        );

    }

    @Test
    public void userBadReleaseDateTest() throws Exception {

        Film film = Film.builder()
                .name("film1")
                .description("decription1")
                .duration(100)
                .releaseDate(LocalDate.parse("1895-12-25"))
                .build();

        final ValidationException exception = assertThrows(
                ValidationException.class,
                new Executable() {
                    @Override
                    public void execute() throws Throwable {

                        Validator.filmValidation(film, films);

                    }
                }
        );

    }

    @Test
    public void filmWrongId() throws Exception {

        Film film = Film.builder()
                .name("film1")
                .description("decription1")
                .duration(100)
                .releaseDate(LocalDate.parse("1895-12-29"))
                .id(1)
                .build();

        final ValidationException exception = assertThrows(
                ValidationException.class,
                new Executable() {
                    @Override
                    public void execute() throws Throwable {

                        Validator.filmValidation(film, films);

                    }
                }
        );

    }

}
