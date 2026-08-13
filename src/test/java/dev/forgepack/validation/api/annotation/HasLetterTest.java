package dev.forgepack.validation.api.annotation;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class HasLetterTest {

    private static Validator validator;

    record Subject(@HasLetter String value) {}

    @BeforeAll
    static void setUpValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void validate_passes_whenNull() {
        assertThat(validator.validate(new Subject(null))).isEmpty();
    }

    @Test
    void validate_passes_whenContainsLetter() {
        assertThat(validator.validate(new Subject("123abc"))).isEmpty();
    }

    @Test
    void validate_passes_whenOnlyLetters() {
        assertThat(validator.validate(new Subject("password"))).isEmpty();
    }

    @Test
    void validate_fails_whenNoLetter() {
        Set<ConstraintViolation<Subject>> violations = validator.validate(new Subject("12345"));
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getPropertyPath().toString()).isEqualTo("value");
    }

    @Test
    void validate_fails_whenEmpty() {
        assertThat(validator.validate(new Subject(""))).hasSize(1);
    }

    @Test
    void validate_fails_whenOnlySpecialChars() {
        assertThat(validator.validate(new Subject("!@#$%"))).hasSize(1);
    }
}
