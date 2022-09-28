package ru.yandex.practicum.filmorate.model;

import lombok.*;

@Data
@Builder
public class Genre extends Model {

    private Long id;
    private String name;

}
