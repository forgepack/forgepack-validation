package dev.forgepack.validation.internal.validator;

import dev.forgepack.validation.api.validator.Validator;
import dev.forgepack.validation.api.validator.ValidatorHasLowerCase;
import jakarta.validation.ConstraintValidatorContext;

public class ValidatorHasLowerCaseImpl implements ValidatorHasLowerCase {

    private final Validator validator = ValidatorImpl.INSTANCE;
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return validator.hasLowerCase(value);
    }
}
