package ru.yandex.practicum.filmorate.model;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString
public class User extends Model {

    private Long id;
    @NotBlank
    @Email
    private String email;
    @NotBlank
    @Pattern(regexp = "\\S+")
    private String login;
    private String name;
    @Past
    private LocalDate birthday;
    private Set<Long> friends;

    public void addFriendOfUser(Long friendId) {

        if (friends == null) {
            friends = new HashSet<>();
        }

        friends.add(friendId);

    }

    public void deleteFriendOfUser(Long friendId) {

        if (friends == null) {
            friends = new HashSet<>();
        }

        friends.remove(friendId);

    }

}
