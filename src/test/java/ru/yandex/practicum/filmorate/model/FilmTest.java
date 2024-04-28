package ru.yandex.practicum.filmorate.model;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class FilmTest {

    private static Validator validator;

   static {
        ValidatorFactory validatorFactory= Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.usingContext().getValidator();
    }

    @Test
    public void validateName(){
        Film film = new Film();
        film.setName(" ");
        film.setDescription("Test");
        film.setDuration(1);

        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertFalse(violations.isEmpty(),"Violation not found");
    }

    @Test
    public void validateDescription(){
        Film film = new Film();
        film.setName("TestName");
        film.setDescription("GGnKrlgVWV!y0N8WKYsV\n" +
                "Qg]o5?HO1]crVyDkLIc3\n" +
                "Ch/wHlSYT'>b)BS]>)6J\n" +
                "wrDR!Q\"&o3/K&kzLi&OE\n" +
                "q_Wf[GBFB>9|W&@1*#q(\n" +
                "YagQoGy@<vLD4l;?]n1u\n" +
                "WF0I|$oklNFPnL;oLenZ\n" +
                "*M].u<*2Ox!2xE9lJT]^\n" +
                "!\\zv.;A1rD<1WpisWR^K\n" +
                "HE#SBbv('tNP3J%3?}@.\n" +
                "_79eJnsY*S:NuC,*kuWG");
        film.setDuration(1);


        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertFalse(violations.isEmpty(),"Violation not found");
    }

    @Test
    public void validateReleaseDate(){
        Film film = new Film();
        film.setName("TestName");
        film.setDescription("Test");
        film.setReleaseDate(LocalDate.of(1500,10,10));
        film.setDuration(1);

        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertFalse(violations.isEmpty(),"Violation not found");
    }

    @Test
    public void validateDuration(){
        Film film = new Film();
        film.setName("TestName");
        film.setDescription("Test");
        film.setDuration(-1);

        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertFalse(violations.isEmpty(),"Violation not found");
    }
}