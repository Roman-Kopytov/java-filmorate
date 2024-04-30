package ru.yandex.practicum.filmorate.model;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {
    private static Validator validator;

    static {
        ValidatorFactory validatorFactory= Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.usingContext().getValidator();
    }


    @Test
    public void validateLogin(){
        User user = new User();
        user.setName("TestName");
        user.setLogin("  ");
        user.setEmail("test@gmail.com");

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty(),"Violation not found");

        user.setLogin("Test Login");
        violations = validator.validate(user);
        assertFalse(violations.isEmpty(),"Violation not found");

    }

    @Test
    public void validateBirthday(){
        User user = new User();
        user.setName("TestName");
        user.setLogin("TestLogin");
        user.setEmail("test@gmail.com");
        user.setBirthday(LocalDate.of(2045,10,10));

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty(),"Violation not found");
    }

}