package ru.yandex.practicum.filmorate.storage.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Component
@Primary
@Slf4j
public class GenreDbStorage implements GenreStorage {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public GenreDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Genre getById(Long id) {

        String sql = "SELECT * FROM GENRE WHERE ID = ?";
        List<Genre> result = jdbcTemplate.query(sql, this::mapToGenre, id);
        if (result.isEmpty()) {
            return null;
        }
        return result.get(0);

    }

    @Override
    public List<Genre> getAll() {
        String sql = "SELECT * FROM GENRE ORDER BY ID";
        return jdbcTemplate.query(sql, this::mapToGenre);
    }

    @Override
    public void add(Genre genre) {

        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("GENRE")
                .usingGeneratedKeyColumns("ID");

        Map<String, Object> values = new HashMap<>();
        values.put("NAME", genre.getName());

        genre.setId(simpleJdbcInsert.executeAndReturnKey(values).longValue());

    }

    @Override
    public void modify(Genre genre) {

        String sql = "UPDATE GENRE SET NAME = ? WHERE ID = ?";
        jdbcTemplate.update(sql, genre.getName(), genre.getId());

    }

    @Override
    public void remove(Long id) {

    }

    private Genre mapToGenre(ResultSet resultSet, int rowNum) throws SQLException {

        return Genre.builder()
                .id(resultSet.getLong("ID"))
                .name(resultSet.getString("NAME"))
                .build();

    }

    @Override
    public Set<Genre> getGenresByFilm(Long filmId) {
        String sql = "SELECT * FROM GENRE AS g INNER JOIN FILM_GENRES AS fg ON g.ID = fg.GENRE_ID AND FILM_ID = ? ORDER BY g.ID";
        return new HashSet<>(jdbcTemplate.query(sql, this::mapToGenre, filmId));
    }

}
