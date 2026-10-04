package dev.forgepack.validation.internal.validator;

import dev.forgepack.validation.api.annotation.HasLength;
import dev.forgepack.validation.api.validator.ValidatorRules;
import dev.forgepack.validation.api.validator.HasLengthValidator;
import jakarta.validation.ConstraintValidatorContext;

public class HasLengthValidatorImpl implements HasLengthValidator {

    private int min = 8;
    private final ValidatorRules validator = ValidatorRulesImpl.INSTANCE;

    @Override
    public void initialize(HasLength annotation) {
        this.min = annotation.min();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) return true;
        return validator.hasLength(min, value);
    }
}
