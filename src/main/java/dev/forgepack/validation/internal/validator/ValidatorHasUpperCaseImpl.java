package dev.forgepack.validation.internal.validator;

import dev.forgepack.validation.api.validator.Validator;
import dev.forgepack.validation.api.validator.ValidatorHasUpperCase;
import jakarta.validation.ConstraintValidatorContext;

public class ValidatorHasUpperCaseImpl implements ValidatorHasUpperCase {

    private final Validator validator = ValidatorImpl.INSTANCE;
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return validator.hasUpperCase(value);
    }
}
