package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.DataNotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;
import ru.yandex.practicum.filmorate.validation.Validator;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class GenreService {
    @Qualifier("GenreDbStorage")
    private final GenreStorage genreStorage;

    public Collection<Genre> getAll() {

        return genreStorage.getAll();

    }

    public Genre getById(Long id) {

        Validator.idValidation(id);

        Genre genre = genreStorage.getById(id);

        if (genre == null) {
            String errorMsg = String.format("Отсутствует жанр с id=%s", id);
            throw new DataNotFoundException(errorMsg);
        }

        return genre;

    }

    public Genre add(Genre genre) {

        genreStorage.add(genre);
        return this.getById(genre.getId());

    }

}
