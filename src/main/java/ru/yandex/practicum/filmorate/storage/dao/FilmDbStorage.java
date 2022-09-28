package ru.yandex.practicum.filmorate.storage.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Component
@Primary
@Slf4j
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void add(Film film) {

        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("FILMS")
                .usingGeneratedKeyColumns("ID");

        Map<String, Object> values = new HashMap<>();
        values.put("NAME", film.getName());
        values.put("DESCRIPTION", film.getDescription());
        values.put("RELEASE_DATE", film.getReleaseDate());
        values.put("DURATION", film.getDuration());
        values.put("MPA_ID", film.getMpa().getId());

        film.setId(simpleJdbcInsert.executeAndReturnKey(values).longValue());

        addGenres(film);

    }

   void addGenres(Film film) {

        String sql = "DELETE FROM FILM_GENRES WHERE FILM_ID = ?";
        jdbcTemplate.update(sql, film.getId());

        sql = "INSERT INTO FILM_GENRES (FILM_ID, GENRE_ID) VALUES(?, ?)";

        Set<Genre> genres = film.getGenres();
        if (genres == null) {
            return;
        }

        for (Genre genre : genres) {
            jdbcTemplate.update(sql, film.getId(), genre.getId());
        }

    }

    @Override
    public void remove(Long filmId) {

        final String sql = "DELETE FROM FILMS WHERE ID = ?";
        jdbcTemplate.update(sql, filmId);

    }

    @Override
    public Collection<Film> getAll() {

        String sql =
                "SELECT f.ID, f.NAME, f.DESCRIPTION, f.RELEASE_DATE, f.DURATION, f.MPA_ID, m.NAME AS MPA_NAME " +
                        "FROM FILMS f JOIN MPA m ON f.MPA_ID = m.ID ORDER BY f.ID";
        return jdbcTemplate.query(sql, this::mapToFilm);

    }

    @Override
    public void modify(Film film) {

        String sql =
                "UPDATE FILMS SET NAME = ?, DESCRIPTION = ?, RELEASE_DATE = ?, DURATION = ?, MPA_ID = ? " +
                        "WHERE ID = ?";
        jdbcTemplate.update(sql, film.getName(), film.getDescription(), film.getReleaseDate(), film.getDuration(),
                film.getMpa().getId(), film.getId());

        addGenres(film);

    }

    @Override
    public Film getById(Long filmId) {

        String sql =
                "SELECT f.ID, f.NAME, f.DESCRIPTION, f.RELEASE_DATE, f.DURATION, f.MPA_ID, m.NAME AS MPA_NAME " +
                        "FROM FILMS AS f LEFT JOIN MPA m ON f.MPA_ID = m.ID " +
                        "WHERE f.ID = ?";
        List<Film> result = jdbcTemplate.query(sql, this::mapToFilm, filmId);
        if (result.isEmpty()) {
            return null;
        }

        return result.get(0);

    }

    private Film mapToFilm(ResultSet resultSet, int rowNum) throws SQLException {

        Mpa mpa = Mpa.builder()
                .id(resultSet.getLong("MPA_ID"))
                .name(resultSet.getString("MPA_NAME"))
                .build();

        return Film.builder()
                .id(resultSet.getLong("ID"))
                .name(resultSet.getString("NAME"))
                .description(resultSet.getString("DESCRIPTION"))
                .duration(resultSet.getInt("DURATION"))
                .releaseDate(resultSet.getDate("RELEASE_DATE").toLocalDate())
                .mpa(mpa)
                .build();

    }

    @Override
    public List<Long> getLikes(Long filmId) {

        String sql = "SELECT USER_ID FROM FILM_LIKES WHERE FILM_ID = ?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> rs.getLong("USER_ID"), filmId);

    }

    @Override
    public void saveLikes(Film film) {

        jdbcTemplate.update("DELETE FROM FILM_LIKES WHERE FILM_ID = ?", film.getId());

        String sql = "INSERT INTO FILM_LIKES (FILM_ID, USER_ID) VALUES(?, ?)";
        Set<Long> likes = film.getLikes();
        for (var like : likes) {
            jdbcTemplate.update(sql, film.getId(), like);
        }

    }
}
