package ru.yandex.practicum.filmorate.exception;

public class NotFoundException extends RuntimeException {
    private String message;

    public NotFoundException(String message) {
        super(message);
    }
}
