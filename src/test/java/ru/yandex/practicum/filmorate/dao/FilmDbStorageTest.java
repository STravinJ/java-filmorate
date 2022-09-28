package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.GenreStorage;
import ru.yandex.practicum.filmorate.storage.dao.FilmDbStorage;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class FilmDbStorageTest {
    private final FilmDbStorage filmStorage;
    private final GenreStorage genreStorage;
    private Film createFilm1() {

        Mpa mpa = Mpa.builder()
                .id(1L)
                .name("G")
                .build();

        Film film = Film.builder()
                .name("film1")
                .description("decription1")
                .duration(100)
                .releaseDate(LocalDate.parse("1895-12-29"))
                .mpa(mpa)
                .build();

        film.setGenres(new HashSet<>());

        return film;
    }

    private Film createFilm2() {

        Mpa mpa = Mpa.builder()
                .id(2L)
                .name("PG")
                .build();

        Film film = Film.builder()
                .name("film2")
                .description("decription2")
                .duration(100)
                .releaseDate(LocalDate.parse("1895-12-28"))
                .mpa(mpa)
                .build();

        film.setGenres(new HashSet<>());

        return film;
    }

    @Test
    void add() {

        Film film1 = createFilm1();
        filmStorage.add(film1);
        Film storFilm = filmStorage.getById(film1.getId());
        assertEquals(film1.getId(), storFilm.getId());
        assertEquals(film1.getName(), storFilm.getName());
        assertEquals(film1.getDescription(), storFilm.getDescription());
        assertEquals(film1.getReleaseDate(), storFilm.getReleaseDate());
        assertEquals(film1.getDuration(), storFilm.getDuration());
        assertEquals(film1.getMpa(), storFilm.getMpa());

    }

    @Test
    void getById() {

        Film film1 = createFilm1();
        filmStorage.add(film1);
        Film storFilm = filmStorage.getById(film1.getId());
        assertEquals(film1.getId(), storFilm.getId());
        assertEquals(film1.getName(), storFilm.getName());

    }

    @Test
    void findAll() {

        Film film1 = createFilm1();
        filmStorage.add(film1);
        Film film2 = createFilm2();
        filmStorage.add(film2);
        Collection<Film> films = List.of(film1, film2);

        Collection<Film> storFilms = filmStorage.getAll();

        for (Film film : storFilms) {
            film.setGenres(genreStorage.getGenresByFilm(film.getId()));
        }
        assertEquals(films.toString(), storFilms.toString());
        assertEquals(2, storFilms.size());
    }

    @Test
    void modify() {

        Film film1 = createFilm1();
        filmStorage.add(film1);
        film1.setName("Super Film");

        filmStorage.modify(film1);
        Film storFilm = filmStorage.getById(film1.getId());

        storFilm.setGenres(genreStorage.getGenresByFilm(storFilm.getId()));

        assertEquals(film1.getId(), storFilm.getId());
        assertEquals(film1.getName(), storFilm.getName());

    }

}
