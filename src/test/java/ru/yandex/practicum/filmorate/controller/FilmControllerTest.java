package ru.yandex.practicum.filmorate.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest
@AutoConfigureMockMvc
class FilmControllerTest {

    Gson gson = new GsonBuilder()
            .registerTypeAdapter(
                    LocalDate.class,
                    (JsonDeserializer<LocalDate>) (json, type, context) -> LocalDate.parse(json.getAsString())
            )
            .registerTypeAdapter(
                    LocalDate.class,
                    (JsonSerializer<LocalDate>) (srs, typeOfSrs, context) -> new JsonPrimitive(srs.toString())
            )
            .create();

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void filmPostWrongTest() throws Exception {

        MvcResult mvcResult = mockMvc.perform(post("/films")
                )
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andReturn();

    }

    @Test
    public void filmPostPutGetTest() throws Exception {

        Film film = Film.builder()
                .name("45")
                .description("decription1")
                .duration(100)
                .releaseDate(LocalDate.parse("1895-12-29"))
                .build();

        String json = gson.toJson(film);

        MvcResult mvcResult = mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(json))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andReturn();

        Film film2 = gson.fromJson(mvcResult.getResponse().getContentAsString(), Film.class);

        assertEquals(film.getName(), film2.getName(), "Имя не соответствует переданному");
        assertEquals(film.getDescription(), film2.getDescription(), "Описание не соответствует переданному");
        assertEquals(film.getDuration(), film2.getDuration(), "Длительность не соответствует переданному");
        assertEquals(film.getReleaseDate(), film2.getReleaseDate(), "Дата фильма не соответствует переданному");

        film2.setName("88");

        json = gson.toJson(film2);

        MvcResult mvcResultPut = mockMvc.perform(put("/films")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(json))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andReturn();

        MvcResult mvcResultGet = mockMvc.perform(get("/films"))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andReturn();

        List<Film> films = new ArrayList<>();

        JsonElement jsonElement = JsonParser.parseString(mvcResultGet.getResponse().getContentAsString());
        if (jsonElement.isJsonArray()) {
            JsonArray jsonArray = jsonElement.getAsJsonArray();
            for (JsonElement jsonElementofArray : jsonArray) {
                if (jsonElementofArray.isJsonObject()) {
                    JsonObject jsonObject = jsonElementofArray.getAsJsonObject();
                    Film newFilm = gson.fromJson(jsonObject, Film.class);
                    films.add(newFilm);
                }
            }
        }

        assertEquals(1, films.size(), "Количество фильмов не соответствует переданному");
        assertEquals("88", films.get(0).getName(), "Название фильма не обновилось");

    }

}