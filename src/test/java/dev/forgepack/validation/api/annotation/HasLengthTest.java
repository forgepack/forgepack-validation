package dev.forgepack.validation.api.annotation;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class HasLengthTest {

    private static Validator validator;

    record DefaultSubject(@HasLength String value) {}

    record CustomSubject(@HasLength(min = 4) String value) {}

    @BeforeAll
    static void setUpValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    // ─── default min (8) ──────────────────────────────────────────────────────

    @Test
    void validate_passes_whenNull() {
        assertThat(validator.validate(new DefaultSubject(null))).isEmpty();
    }

    @Test
    void validate_passes_whenExactlyEightChars() {
        assertThat(validator.validate(new DefaultSubject("12345678"))).isEmpty();
    }

    @Test
    void validate_passes_whenMoreThanEightChars() {
        assertThat(validator.validate(new DefaultSubject("123456789"))).isEmpty();
    }

    @Test
    void validate_fails_whenLessThanEightChars() {
        Set<ConstraintViolation<DefaultSubject>> violations = validator.validate(new DefaultSubject("1234567"));
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getPropertyPath().toString()).isEqualTo("value");
    }

    @Test
    void validate_fails_whenEmpty() {
        assertThat(validator.validate(new DefaultSubject(""))).hasSize(1);
    }

    // ─── custom min ───────────────────────────────────────────────────────────

    @Test
    void validate_passes_withCustomMin_whenExact() {
        assertThat(validator.validate(new CustomSubject("abcd"))).isEmpty();
    }

    @Test
    void validate_fails_withCustomMin_whenTooShort() {
        assertThat(validator.validate(new CustomSubject("abc"))).hasSize(1);
    }

    @Test
    void validate_passes_withCustomMin_whenNull() {
        assertThat(validator.validate(new CustomSubject(null))).isEmpty();
    }
}
