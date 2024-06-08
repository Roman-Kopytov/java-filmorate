package ru.yandex.practicum.filmorate.exception;

public class BadBodyRequestException extends RuntimeException {

    private String message;

    public BadBodyRequestException(String message) {
        super(message);
    }


}
