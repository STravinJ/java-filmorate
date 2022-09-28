package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.DataNotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.MpaStorage;
import ru.yandex.practicum.filmorate.validation.Validator;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class MpaService {

    @Qualifier("MpaDbStorage")
    private final MpaStorage mpaStorage;

    public Collection<Mpa> getAll() {

        return mpaStorage.getAll();

    }

    public Mpa getById(Long id) {

        Validator.idValidation(id);

        Mpa mpa = mpaStorage.getById(id);

        if (mpa == null) {
            String errorMsg = String.format("Отсутствует рейтинг с id=%s", id);
            throw new DataNotFoundException(errorMsg);
        }

        return mpa;

    }

    public Mpa add(Mpa mpa) {

        mpaStorage.add(mpa);
        return this.getById(mpa.getId());

    }

}
