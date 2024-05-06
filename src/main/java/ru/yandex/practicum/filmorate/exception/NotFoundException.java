package ru.yandex.practicum.filmorate.exception;

public class NotFoundException extends RuntimeException {
    private String massage;

    public NotFoundException(String massage) {
        super(massage);
    }
}
