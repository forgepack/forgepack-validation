package dev.forgepack.validation.api.annotation;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class HasLowerCaseTest {

    private static Validator validator;

    record Subject(@HasLowerCase String value) {}

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
    void validate_passes_whenContainsLowerCase() {
        assertThat(validator.validate(new Subject("PASSword"))).isEmpty();
    }

    @Test
    void validate_passes_whenAllLowerCase() {
        assertThat(validator.validate(new Subject("password"))).isEmpty();
    }

    @Test
    void validate_fails_whenNoLowerCase() {
        Set<ConstraintViolation<Subject>> violations = validator.validate(new Subject("PASSWORD1"));
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getPropertyPath().toString()).isEqualTo("value");
    }

    @Test
    void validate_fails_whenEmpty() {
        assertThat(validator.validate(new Subject(""))).hasSize(1);
    }

    @Test
    void validate_fails_whenOnlyDigits() {
        assertThat(validator.validate(new Subject("12345"))).hasSize(1);
    }
}
