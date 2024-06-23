package ru.yandex.practicum.filmorate.exception;

public class WrongRequestParam extends RuntimeException {

    public WrongRequestParam(String message) {
        super(message);
    }
}
