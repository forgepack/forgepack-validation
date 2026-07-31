package dev.forgepack.validation.internal.validator;

import dev.forgepack.validation.api.validator.Validator;
import dev.forgepack.validation.api.validator.ValidatorHasLetter;
import jakarta.validation.ConstraintValidatorContext;

public class ValidatorHasLetterImpl implements ValidatorHasLetter {

    private final Validator validator = ValidatorImpl.INSTANCE;
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return validator.hasLetter(value);
    }
}
