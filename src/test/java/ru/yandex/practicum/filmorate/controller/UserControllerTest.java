package ru.yandex.practicum.filmorate.controller;

import com.google.gson.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@WebMvcTest(controllers = UserController.class)
class UserControllerTest {

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
    public void userPostWrongTest() throws Exception {

        MvcResult mvcResult = mockMvc.perform(post("/users")
                )
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andReturn();

    }

    @Test
    public void userPostPutGetTest() throws Exception {

        User user = User.builder()
                .name("user1")
                .login("login1")
                .email("test@test.ru")
                .birthday(LocalDate.parse("1895-12-28"))
                .build();

        String json = gson.toJson(user);

        MvcResult mvcResult = mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(json))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andReturn();

        User user2 = gson.fromJson(mvcResult.getResponse().getContentAsString(), User.class);

        assertEquals(user.getName(), user2.getName(), "Имя не соответствует переданному");
        assertEquals(user.getBirthday(), user2.getBirthday(), "Дата рождения не соответствует переданному");
        assertEquals(user.getLogin(), user2.getLogin(), "Логин не соответствует переданному");
        assertEquals(user.getEmail(), user2.getEmail(), "Почта не соответствует переданному");

        user2.setName("user11");

        json = gson.toJson(user2);

        MvcResult mvcResultPut = mockMvc.perform(put("/users")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(json))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andReturn();

        MvcResult mvcResultGet = mockMvc.perform(get("/users"))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andReturn();

        List<User> users = new ArrayList<>();

        JsonElement jsonElement = JsonParser.parseString(mvcResultGet.getResponse().getContentAsString());
        if (jsonElement.isJsonArray()) {
            JsonArray jsonArray = jsonElement.getAsJsonArray();
            for (JsonElement jsonElementofArray : jsonArray) {
                if (jsonElementofArray.isJsonObject()) {
                    JsonObject jsonObject = jsonElementofArray.getAsJsonObject();
                    User newUser = gson.fromJson(jsonObject, User.class);
                    users.add(newUser);
                }
            }
        }

        assertEquals(1, users.size(), "Количество фильмов не соответствует переданному");
        assertEquals("user11", users.get(0).getName(), "Имя пользователя не обновилось");

    }

}