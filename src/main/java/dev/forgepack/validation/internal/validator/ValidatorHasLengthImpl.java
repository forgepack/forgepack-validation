package dev.forgepack.validation.internal.validator;

import dev.forgepack.validation.api.annotation.HasLength;
import dev.forgepack.validation.api.validator.Validator;
import dev.forgepack.validation.api.validator.ValidatorHasLength;
import jakarta.validation.ConstraintValidatorContext;

public class ValidatorHasLengthImpl implements ValidatorHasLength {

    private int min = 8;
    private final Validator validator = ValidatorImpl.INSTANCE;

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
