package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.validation.Validator;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
@RestControllerAdvice
@Slf4j
public class InMemoryUserStorage implements UserStorage {

    private final Map<Long, User> users = new HashMap<>();
    private Long idUser = 0L;

    @Override
    public void add(User user) {

        Validator.userValidation(user, users);

        user.setId(++idUser);
        users.put(user.getId(), user);

        log.info("Добавление пользователя: {}", user);

    }

    @Override
    public void remove(Long userId) {

        users.remove(userId);

    }

    @Override
    public User getById(Long userId) {

        log.info("Получение пользователя по id.");

        Validator.userIdValidation(userId, users);

        return users.get(userId);

    }

    @Override
    public Collection<User> getAll() {

        log.info("Получение всех пользователей.");
        return users.values();

    }

    @Override
    public void modifyUser(User user) {

        Validator.userValidation(user, users);

        users.remove(user.getId());
        users.put(user.getId(), user);

        log.info("Обновленние пользователя: {}", user);

    }

}
