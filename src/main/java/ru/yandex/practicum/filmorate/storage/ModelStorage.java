package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Model;

import java.util.Collection;

public interface ModelStorage<M extends Model> {

    M getById(Long id);

    Collection<M> getAll();

    void add(M data);

    void modify(M data);

    void remove(Long id);

}