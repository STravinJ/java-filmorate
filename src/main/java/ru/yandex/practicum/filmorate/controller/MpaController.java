package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.MpaService;

import java.util.Collection;

@RestController
@RequiredArgsConstructor
@RequestMapping("/mpa")
public class MpaController {

    private final MpaService mpaService;

    @GetMapping
    public Collection<Mpa> getAll() {

        return mpaService.getAll();

    }

    @GetMapping("/{id}")
    public Mpa getById(@PathVariable long id) {
        return mpaService.getById(id);
    }

    @PostMapping
    public Mpa create(@RequestBody Mpa mpa) {

        return mpaService.add(mpa);

    }

}
