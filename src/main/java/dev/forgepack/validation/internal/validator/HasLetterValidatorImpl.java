package dev.forgepack.validation.internal.validator;

import dev.forgepack.validation.api.validator.ValidatorRules;
import dev.forgepack.validation.api.validator.HasLetterValidator;
import jakarta.validation.ConstraintValidatorContext;

public class HasLetterValidatorImpl implements HasLetterValidator {

    private final ValidatorRules validator = ValidatorRulesImpl.INSTANCE;
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) return true;
        return validator.hasLetter(value);
    }
}
