package dev.forgepack.validation.internal.validator;

import dev.forgepack.validation.api.annotation.Unique;
import dev.forgepack.validation.api.service.UniqueCheckableService;
import dev.forgepack.validation.api.validator.UniqueValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.context.ApplicationContext;
import java.lang.reflect.Field;
import java.util.UUID;

public class UniqueValidatorImpl implements UniqueValidator {

    private String[] fields;
    private String idField;
    private Class<? extends UniqueCheckableService> serviceClass;
    private UniqueCheckableService service;
    private final ApplicationContext context;

    public UniqueValidatorImpl(ApplicationContext context) {
        this.context = context;
    }

    @Override
    public void initialize(Unique annotation) {
        this.fields = annotation.fields();
        this.idField = annotation.idField();
        this.serviceClass = annotation.service();
        this.service = context.getBean(annotation.service());
    }
    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (value == null) return true;
        Object idValue = getFieldValue(value, idField);
        boolean allUnique = true;
        for (String field : fields) {
            Object fieldValue = getFieldValue(value, field);
            if (fieldValue == null) continue;
            String trimmed = fieldValue.toString().trim();
            if (trimmed.isBlank()) continue;
            boolean isUnique = (idValue == null || idValue.toString().isBlank())
                    ? !resolveService().existsByField(field, trimmed)
                    : !resolveService().existsByFieldAndIdNot(field, trimmed, toUUID(idValue));
            if (!isUnique) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate(
                                context.getDefaultConstraintMessageTemplate()
                                        .replace("{field}", field)
                        ).addPropertyNode(field)
                        .addConstraintViolation();
                allUnique = false;
            }
        }
        return allUnique;
    }
    private UniqueCheckableService resolveService() {
        if (service == null) {
            service = context.getBean(serviceClass);
        }
        return service;
    }
    private Object getFieldValue(Object target, String fieldName) {
        try {
            Field f = target.getClass().getDeclaredField(fieldName);
            f.setAccessible(true);
            return f.get(target);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new IllegalArgumentException(
                    "Field '" + fieldName + "' not found in " + target.getClass().getSimpleName()
            );
        }
    }

    private UUID toUUID(Object value) {
        if (value instanceof UUID uuid) return uuid;
        try {
            return UUID.fromString(value.toString());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(
                    "The idField value '" + value + "' cannot be converted to UUID. " +
                    "@Unique only supports UUID identifiers.", e
            );
        }
    }
}
