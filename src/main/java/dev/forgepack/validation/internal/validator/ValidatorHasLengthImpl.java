package dev.forgepack.validation.internal.validator;

import dev.forgepack.validation.api.validator.Validator;
import dev.forgepack.validation.api.validator.ValidatorHasLength;
import jakarta.validation.ConstraintValidatorContext;

public class ValidatorHasLengthImpl implements ValidatorHasLength {

    private final Validator validator = ValidatorImpl.INSTANCE;
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return validator.hasLength(8, value);
    }
}
