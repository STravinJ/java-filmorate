package ru.yandex.practicum.filmorate.storage.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.MpaStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Primary
@Slf4j
public class MpaDbStorage implements MpaStorage {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public  MpaDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Mpa getById(Long id) {

        String sql = "SELECT * FROM MPA WHERE ID = ?";
        List<Mpa> result = jdbcTemplate.query(sql, this::mapToMpa, id);
        if (result.isEmpty()) {
            return null;
        }
        return result.get(0);

    }

    @Override
    public List<Mpa> getAll() {
        String sql = "SELECT * FROM MPA ORDER BY ID";
        return jdbcTemplate.query(sql, this::mapToMpa);
    }

    @Override
    public void add(Mpa mpa) {

        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("MPA")
                .usingGeneratedKeyColumns("ID");

        Map<String, Object> values = new HashMap<>();
        values.put("NAME", mpa.getName());

        mpa.setId(simpleJdbcInsert.executeAndReturnKey(values).longValue());

    }

    @Override
    public void modify(Mpa mpa) {

        String sql = "UPDATE MPA SET NAME = ? WHERE ID = ?";
        jdbcTemplate.update(sql, mpa.getName(), mpa.getId());

    }

    @Override
    public void remove(Long id) {

    }

    private Mpa mapToMpa(ResultSet resultSet, int rowNum) throws SQLException {

        return Mpa.builder()
                .id(resultSet.getLong("ID"))
                .name(resultSet.getString("NAME"))
                .build();

    }

}