package ru.yandex.practicum.filmorate.validation;

import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Map;

public class Validator {

    public void filmValidation(Film film, Map<Integer, Film> films) {

        if (film.getName().equals("")) {
            throw new ValidationException("Название фильма не может быть пустым.");
        }

        if (film.getDescription().length() > 200) {
            throw new ValidationException("Описание не может быть более 200 символова.");
        }

        if (film.getId() != null && !films.containsKey(film.getId())) {
            throw new ValidationException("Не найден фильм при обновлении.");
        }

        if (film.getReleaseDate().isBefore(LocalDate.parse("1895-12-28"))) {
            throw new ValidationException("Дата фильма не может быть раньше даты зарождения кино.");
        }

        if (film.getDuration() < 0) {
            throw new ValidationException("Продолжительность не может быть отрицательной.");
        }

    }

    public void userValidation(User user, Map<Integer, User> users) {

        if (user.getEmail() == null || user.getEmail().isBlank()) {
            throw new ValidationException("Адрес электронной почты не может быть пустым.");
        }

        if (!user.getEmail().contains("@")) {
            throw new ValidationException("Email должен содержать символ @.");
        }

        if (user.getId() != null && !users.containsKey(user.getId())) {
            throw new ValidationException("Не найден пользователь при обновлении.");
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

}
