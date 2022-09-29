package ru.yandex.practicum.filmorate.validation;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import ru.yandex.practicum.filmorate.exception.DataNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class UserValidatorTest {

    Map<Long, User> users = new HashMap<>();

    private User createUser() {

        User user = new User();
        user.setName("user1");
        user.setLogin("user1");
        user.setEmail("testtest.ru");
        user.setBirthday(LocalDate.parse("1895-12-28"));

        return user;
    }
    @Test
    public void userBadEmailTest() throws Exception {

        User user = createUser();
        user.setEmail("");

        final ValidationException exception = assertThrows(
                ValidationException.class,
                new Executable() {
                    @Override
                    public void execute() throws Throwable {

                        Validator.userValidation(user);

                    }
                }
        );

    }

    @Test
    public void userBadEmailTestSecond() throws Exception {

        User user = createUser();
        user.setEmail("testtest.ru");

        final ValidationException exception = assertThrows(
                ValidationException.class,
                new Executable() {
                    @Override
                    public void execute() throws Throwable {

                        Validator.userValidation(user);

                    }
                }
        );

    }

    @Test
    public void userBadLogin() throws Exception {

        User user = createUser();
        user.setLogin("");

        final ValidationException exception = assertThrows(
                ValidationException.class,
                new Executable() {
                    @Override
                    public void execute() throws Throwable {

                        Validator.userValidation(user);

                    }
                }
        );

    }

    @Test
    public void userWithoutName() throws Exception {

        User user = createUser();
        user.setName("");

        Validator.userValidation(user);

        assertEquals(user.getName(), user.getLogin(), "Имя не обновилось");

    }

    @Test
    public void userFuturebirthday() throws Exception {

        User user = createUser();
        user.setBirthday(LocalDate.parse("2023-12-28"));

        final ValidationException exception = assertThrows(
                ValidationException.class,
                new Executable() {
                    @Override
                    public void execute() throws Throwable {

                        Validator.userValidation(user);

                    }
                }
        );

    }

    @Test
    public void userWrongId() throws Exception {

        User user = createUser();
        user.setId(-1L);

        final DataNotFoundException exception = assertThrows(
                DataNotFoundException.class,
                new Executable() {
                    @Override
                    public void execute() throws Throwable {

                        Validator.idValidation(user.getId());

                    }
                }
        );

    }

}
