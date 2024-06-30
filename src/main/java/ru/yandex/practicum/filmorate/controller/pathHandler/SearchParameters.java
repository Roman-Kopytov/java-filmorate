package ru.yandex.practicum.filmorate.controller.pathHandler;

import ru.yandex.practicum.filmorate.exception.WrongRequestParam;

import java.util.Arrays;
import java.util.List;

public class SearchParameters {

    public static String by(String params) {
        List<String> newParams = Arrays.stream(params.toLowerCase().split(",")).toList();
        if (newParams.size() > 2) {
            throw new WrongRequestParam("Only 2 parameters are allowed: director, title");
        }
        return switch (params) {
            case "director,title", "title,director" -> "director,title";
            case "director" -> "director";
            case "title" -> "title";
            default -> throw new WrongRequestParam("Only 2 parameters are allowed: director, title");
        };
    }
}
