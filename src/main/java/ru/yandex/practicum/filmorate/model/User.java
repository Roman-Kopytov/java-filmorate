package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDate;

@Data
public class User {
    private int id;
    @NotBlank
    @Email
    private String email;
    @NotBlank
    private String login;
    private String name;
    private LocalDate birthday;

    @AssertTrue
    public boolean isValidBirthday() {
        if (birthday != null) {
            return birthday.isBefore(LocalDate.now());
        } else return true;
    }

    @AssertTrue
    public boolean isValidLogin() {
        return !login.contains(" ");
    }
}
