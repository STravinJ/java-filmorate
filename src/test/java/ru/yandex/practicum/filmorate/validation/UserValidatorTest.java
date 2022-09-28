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

    Validator validator = new Validator();
    Map<Long, User> users = new HashMap<>();

    @Test
    public void userBadEmailTest() throws Exception {

        User user = User.builder()
                .name("user1")
                .login("user1")
                .email("")
                .birthday(LocalDate.parse("1895-12-28"))
                .build();

        final ValidationException exception = assertThrows(
                ValidationException.class,
                new Executable() {
                    @Override
                    public void execute() throws Throwable {

                        validator.userValidation(user);

                    }
                }
        );

    }

    @Test
    public void userBadEmailTestSecond() throws Exception {

        User user = User.builder()
                .name("user1")
                .login("user1")
                .email("testtest.ru")
                .birthday(LocalDate.parse("1895-12-28"))
                .build();

        final ValidationException exception = assertThrows(
                ValidationException.class,
                new Executable() {
                    @Override
                    public void execute() throws Throwable {

                        validator.userValidation(user);

                    }
                }
        );

    }

    @Test
    public void userBadLogin() throws Exception {

        User user = User.builder()
                .name("user1")
                .login("")
                .email("test@test.ru")
                .birthday(LocalDate.parse("1895-12-28"))
                .build();

        final ValidationException exception = assertThrows(
                ValidationException.class,
                new Executable() {
                    @Override
                    public void execute() throws Throwable {

                        validator.userValidation(user);

                    }
                }
        );

    }

    @Test
    public void userWithoutName() throws Exception {

        User user = User.builder()
                .name("")
                .login("user1")
                .email("test@test.ru")
                .birthday(LocalDate.parse("1895-12-28"))
                .build();

        validator.userValidation(user);

        assertEquals(user.getName(), user.getLogin(), "Имя не обновилось");

    }

    @Test
    public void userFuturebirthday() throws Exception {

        User user = User.builder()
                .name("user1")
                .login("")
                .email("test@test.ru")
                .birthday(LocalDate.parse("2023-12-28"))
                .build();

        final ValidationException exception = assertThrows(
                ValidationException.class,
                new Executable() {
                    @Override
                    public void execute() throws Throwable {

                        validator.userValidation(user);

                    }
                }
        );

    }

    @Test
    public void userWrongId() throws Exception {

        User user = User.builder()
                .name("user1")
                .login("")
                .email("test@test.ru")
                .birthday(LocalDate.parse("1895-12-28"))
                .id(-1L)
                .build();

        final DataNotFoundException exception = assertThrows(
                DataNotFoundException.class,
                new Executable() {
                    @Override
                    public void execute() throws Throwable {

                        validator.idValidation(user.getId());

                    }
                }
        );

    }

}
