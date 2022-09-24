package ru.yandex.practicum.filmorate.service;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FilmServiceTest {

    private static FilmService filmService;
    private static Film film;
    private static Film film2;

    @BeforeAll
    static void BeforeAll() {

        FilmStorage filmStorage = new InMemoryFilmStorage();
        filmService = new FilmService(filmStorage);

        film = Film.builder()
                .name("film1")
                .description("decription1")
                .duration(100)
                .releaseDate(LocalDate.parse("1895-12-29"))
                .build();

        film2 = Film.builder()
                .name("film2")
                .description("decription2")
                .duration(100)
                .releaseDate(LocalDate.parse("1895-12-29"))
                .build();

        filmService.addFilm(film);
        filmService.addFilm(film2);

    }

    @Test
    void addLike() {

        filmService.addLike(1L, 1L);

        assertEquals(1, film.getUsersLikes().size(), "Количество лайков не совпадает.");
        assertTrue(film.getUsersLikes().contains(1L), "Id пользователя не совпадает.");

        filmService.deleteLike(1L, 1L);

    }

    @Test
    void deleteLike() {

        filmService.addLike(1L, 1L);
        filmService.deleteLike(1L, 1L);

        assertEquals(0, film.getUsersLikes().size(), "Количество лайков не совпадает.");

    }

    @Test
    void getTopLikedFilms() {

        filmService.addLike(1L, 1L);
        filmService.addLike(1L, 2L);
        filmService.addLike(2L, 1L);

        assertEquals(1, filmService.getPopularFilms(1).size(), "Количество лайков не совпадает.");
        assertTrue(filmService.getPopularFilms(1).contains(film), "Id пользователя не совпадает.");

        filmService.deleteLike(1L, 1L);
        filmService.deleteLike(1L, 2L);
        filmService.deleteLike(2L, 1L);

    }

}
