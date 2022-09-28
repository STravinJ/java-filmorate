package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.GenreService;

import java.util.Collection;

@RestController
@RequiredArgsConstructor
@RequestMapping("/genres")
public class GenreController {

    private final GenreService genreService;

    @GetMapping
    public Collection<Genre> getAll() {

        return genreService.getAll();

    }

    @GetMapping("/{id}")
    public Genre getById(@PathVariable long id) {
        return genreService.getById(id);
    }

    @PostMapping
    public Genre create(@RequestBody Genre genre) {

        return genreService.add(genre);

    }

}
