package dev.forgepack.validation.internal.validator;

import dev.forgepack.validation.api.annotation.Unique;
import dev.forgepack.validation.api.service.ServiceUniqueCheckable;
import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationContext;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ValidatorUniqueImplTest {

    @Mock ApplicationContext applicationContext;
    @Mock ServiceUniqueCheckable service;
    @Mock ConstraintValidatorContext constraintContext;
    @Mock ConstraintValidatorContext.ConstraintViolationBuilder violationBuilder;
    @Mock ConstraintValidatorContext.ConstraintViolationBuilder.NodeBuilderCustomizableContext nodeBuilder;
    @Mock Unique annotation;

    private ValidatorUniqueImpl sut;

    static class NameDto {
        UUID id; // null = create scenario
        String name;
        NameDto(String name) { this.name = name; }
    }

    static class NameWithIdDto {
        UUID id;
        String name;
        NameWithIdDto(UUID id, String name) { this.id = id; this.name = name; }
    }

    static class StringIdDto {
        String id;
        String name;
        StringIdDto(String id, String name) { this.id = id; this.name = name; }
    }

    @SuppressWarnings("unchecked")
    @BeforeEach
    void setUp() {
        sut = new ValidatorUniqueImpl(applicationContext);
        when(annotation.fields()).thenReturn(new String[]{"name"});
        when(annotation.idField()).thenReturn("id");
        when(annotation.service()).thenReturn((Class) ServiceUniqueCheckable.class);
        when(applicationContext.getBean(any(Class.class))).thenReturn(service);
        sut.initialize(annotation);
    }

    // ─── null object ──────────────────────────────────────────────────────────

    @Test
    void isValid_returnsTrue_whenObjectIsNull() {
        assertThat(sut.isValid(null, constraintContext)).isTrue();
        verifyNoInteractions(service);
    }

    // ─── create scenario (no id) ─────────────────────────────────────────────

    @Test
    void isValid_returnsTrue_whenNameIsUnique_create() {
        when(service.existsByField("name", "admin")).thenReturn(false);

        assertThat(sut.isValid(new NameDto("admin"), constraintContext)).isTrue();
    }

    @Test
    void isValid_returnsFalse_whenNameAlreadyExists_create() {
        when(service.existsByField("name", "admin")).thenReturn(true);
        when(constraintContext.getDefaultConstraintMessageTemplate()).thenReturn("unique");
        when(constraintContext.buildConstraintViolationWithTemplate(any())).thenReturn(violationBuilder);
        when(violationBuilder.addPropertyNode(any())).thenReturn(nodeBuilder);

        assertThat(sut.isValid(new NameDto("admin"), constraintContext)).isFalse();
        verify(constraintContext).disableDefaultConstraintViolation();
        verify(nodeBuilder).addConstraintViolation();
    }

    // ─── update scenario (with id) ───────────────────────────────────────────

    @Test
    void isValid_returnsTrue_whenNameIsUnique_update() {
        UUID id = UUID.randomUUID();
        when(service.existsByFieldAndIdNot("name", "admin", id)).thenReturn(false);

        assertThat(sut.isValid(new NameWithIdDto(id, "admin"), constraintContext)).isTrue();
    }

    @Test
    void isValid_returnsFalse_whenNameAlreadyExistsForAnotherRecord_update() {
        UUID id = UUID.randomUUID();
        when(service.existsByFieldAndIdNot("name", "admin", id)).thenReturn(true);
        when(constraintContext.getDefaultConstraintMessageTemplate()).thenReturn("unique");
        when(constraintContext.buildConstraintViolationWithTemplate(any())).thenReturn(violationBuilder);
        when(violationBuilder.addPropertyNode(any())).thenReturn(nodeBuilder);

        assertThat(sut.isValid(new NameWithIdDto(id, "admin"), constraintContext)).isFalse();
    }

    // ─── blank / null field values ───────────────────────────────────────────

    @Test
    void isValid_skipsValidation_whenFieldValueIsNull() {
        assertThat(sut.isValid(new NameDto(null), constraintContext)).isTrue();
        verifyNoInteractions(service);
    }

    @Test
    void isValid_skipsValidation_whenFieldValueIsBlank() {
        assertThat(sut.isValid(new NameDto("   "), constraintContext)).isTrue();
        verifyNoInteractions(service);
    }

    // ─── UUID string conversion ───────────────────────────────────────────────

    @Test
    void isValid_acceptsUuidAsString_forIdField() {
        UUID id = UUID.randomUUID();
        when(service.existsByFieldAndIdNot(eq("name"), eq("admin"), eq(id))).thenReturn(false);

        assertThat(sut.isValid(new StringIdDto(id.toString(), "admin"), constraintContext)).isTrue();
    }

    // ─── field not found ─────────────────────────────────────────────────────

    @Test
    void isValid_throwsIllegalArgument_whenFieldDoesNotExist() {
        assertThatThrownBy(() -> sut.isValid(new Object(), constraintContext))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("id");
    }
}
