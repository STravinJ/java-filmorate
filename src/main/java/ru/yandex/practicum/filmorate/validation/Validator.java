package ru.yandex.practicum.filmorate.validation;

import ru.yandex.practicum.filmorate.exception.DataNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Model;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.ModelStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.Map;

public class Validator {

    public static void filmValidation(Film film) {

        if (film.getName().equals("")) {
            throw new ValidationException("Название фильма не может быть пустым.");
        }

        if (film.getDescription().length() > 200) {
            throw new ValidationException("Описание не может быть более 200 символова.");
        }

        if (film.getReleaseDate().isBefore(LocalDate.parse("1895-12-28"))) {
            throw new ValidationException("Дата фильма не может быть раньше даты зарождения кино.");
        }

        if (film.getDuration() < 0) {
            throw new ValidationException("Продолжительность не может быть отрицательной.");
        }

    }

    public static void userValidation(User user) {

        if (user.getEmail() == null || user.getEmail().isBlank()) {
            throw new ValidationException("Адрес электронной почты не может быть пустым.");
        }

        if (!user.getEmail().contains("@")) {
            throw new ValidationException("Email должен содержать символ @.");
        }

        if (user.getLogin() == null || user.getLogin().isBlank()) {
            throw new ValidationException("Логин не может быть пустым и содержать пробелы.");
        }

        if (user.getName().equals("")) {
            user.setName(user.getLogin());
        }

        int result = LocalDate.now().compareTo(user.getBirthday());
        if (result < 0) {
            throw new ValidationException("Дата рождения не может быть в будущем.");
        }

    }

    public static void userIdValidation(Long userID, Map<Long, User> users) {

        if(!(users.containsKey(userID))) {
            String errorMsg = String.format("Отсутствует пользователь с id=%s", userID);
            throw new DataNotFoundException(errorMsg);
        }
        if(userID < 0) {
            String errorMsg = String.format("Некорректный id=%s", userID);
            throw new ValidationException(errorMsg);
        }

    }

    public static void storageValidation(Model model, ModelStorage modelStorage) {

        Long modelId = model.getId();

        idValidation(modelId);

        if(modelStorage.getById(modelId) == null) {
            String errorMsg = String.format("Отсутствует объект с id=%s", modelId);
            throw new DataNotFoundException(errorMsg);
        }

    }

    public static void storageValidation(Long modelId, ModelStorage modelStorage) {

        idValidation(modelId);

        if(modelStorage.getById(modelId) == null) {
            String errorMsg = String.format("Отсутствует объект с id=%s", modelId);
            throw new DataNotFoundException(errorMsg);
        }

    }

    public static void idValidation(Long id) {

        if(id < 0) {
            String errorMsg = String.format("Некорректный id=%s", id);
            throw new DataNotFoundException(errorMsg);
        }

    }

    public static void filmIdValidation(Long filmID, Map<Long, Film> films) {

        if(!(films.containsKey(filmID))) {
            String errorMsg = String.format("Отсутствует фильм с id=%s", filmID);
            throw new DataNotFoundException(errorMsg);
        }
        if(filmID < 0) {
            String errorMsg = String.format("Некорректный id=%s", filmID);
            throw new ValidationException(errorMsg);
        }

    }

}
