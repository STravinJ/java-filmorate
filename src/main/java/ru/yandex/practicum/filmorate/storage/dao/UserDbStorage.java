package ru.yandex.practicum.filmorate.storage.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Primary
@Slf4j
public class UserDbStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void add(User user) {

        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("USERS")
                .usingGeneratedKeyColumns("ID");

        Map<String, Object> values = new HashMap<>();
        values.put("EMAIL", user.getEmail());
        values.put("LOGIN", user.getLogin());
        values.put("NAME", user.getName());
        values.put("BIRTHDAY", user.getBirthday());

        user.setId(simpleJdbcInsert.executeAndReturnKey(values).longValue());

    }

    @Override
    public void remove(Long userId) {

        final String sql = "DELETE FROM USERS WHERE ID = ?";
        jdbcTemplate.update(sql, userId);

    }

    @Override
    public Collection<User> getAll() {

        String sql = "SELECT * FROM USERS ORDER BY ID";
        Collection<User> users = jdbcTemplate.query(sql, this::mapToUser);

        sql = "(SELECT FRIEND_ID AS ID FROM FRIENDS WHERE USER_ID = ?) " +
                "UNION " +
                "(SELECT USER_ID AS ID FROM FRIENDS WHERE FRIEND_ID = ? AND  IS_CONFIRMED = true)";

        for (User user : users) {

            SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(sql, user.getId(), user.getId());
            while (sqlRowSet.next()) {
                user.addFriendOfUser(sqlRowSet.getLong("ID"));
            }

        }

        return users;

    }

    @Override
    public void modify(User user) {

        String sql =
                "UPDATE USERS SET LOGIN = ?, EMAIL = ?, NAME = ?, BIRTHDAY = ?" +
                        "WHERE ID = ?";
        jdbcTemplate.update(sql, user.getLogin(), user.getEmail(), user.getName(), user.getBirthday(), user.getId());

    }

    @Override
    public User getById(Long userId) {

        String sql = "SELECT * FROM USERS WHERE ID = ?";
        List<User> result = jdbcTemplate.query(sql, this::mapToUser, userId);
        if (result.isEmpty()) {
            return null;
        }

        User user = result.get(0);

        sql = "(SELECT FRIEND_ID AS ID FROM FRIENDS WHERE USER_ID = ?) " +
                "UNION " +
                "(SELECT USER_ID AS ID FROM FRIENDS WHERE FRIEND_ID = ? AND  IS_CONFIRMED = true)";
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(sql, userId, userId);
        while (sqlRowSet.next()) {
            user.addFriendOfUser(sqlRowSet.getLong("ID"));
        }

        return user;

    }

    @Override
    public void addFriend(Long userId, Long friendId) {

        String sql = "INSERT INTO FRIENDS (USER_ID, FRIEND_ID, IS_CONFIRMED) VALUES(?, ?, ?)";
        jdbcTemplate.update(sql, userId, friendId, false);

    }

    @Override
    public void modifyFriend(Long userId, Long friendId, boolean confirmed, Long userIdBefore, Long friendIdBefore) {

        String sql =
                "UPDATE FRIENDS SET USER_ID = ?, FRIEND_ID = ?, IS_CONFIRMED = ? " +
                        "WHERE USER_ID = ? AND FRIEND_ID = ?";
        jdbcTemplate.update(sql, userId, friendId, confirmed, userIdBefore, friendIdBefore);

    }

    @Override
    public void removeFriend(Long userId, Long friendId) {

        String sql = "DELETE FROM FRIENDS WHERE USER_ID = ? AND FRIEND_ID = ?";
        jdbcTemplate.update(sql, userId, friendId);

    }

    @Override
    public boolean getFriends(Long userId, Long friendId, Boolean isConfirmed) {

        String sql = "SELECT * FROM FRIENDS WHERE USER_ID = ? AND FRIEND_ID = ? AND  IS_CONFIRMED = ?";
        SqlRowSet rows = jdbcTemplate.queryForRowSet(sql, userId, friendId, isConfirmed);
        return rows.next();

    }

    @Override
    public List<User> getFriends(Long userId) {

        String sql = "SELECT * FROM USERS AS u INNER JOIN FRIENDS AS f ON u.ID = f.FRIEND_ID AND f.USER_ID = ?";
        return jdbcTemplate.query(sql, this::mapToUser, userId);

    }

    private User mapToUser(ResultSet resultSet, int rowNum) throws SQLException {

        User user = new User();
        user.setId(resultSet.getLong("ID"));
        user.setName(resultSet.getString("NAME"));
        user.setLogin(resultSet.getString("LOGIN"));
        user.setEmail(resultSet.getString("EMAIL"));
        user.setBirthday(resultSet.getDate("BIRTHDAY").toLocalDate());

        return user;

    }

}
