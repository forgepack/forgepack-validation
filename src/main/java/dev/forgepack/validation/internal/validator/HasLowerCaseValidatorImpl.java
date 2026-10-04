package dev.forgepack.validation.internal.validator;

import dev.forgepack.validation.api.validator.ValidatorRules;
import dev.forgepack.validation.api.validator.HasLowerCaseValidator;
import jakarta.validation.ConstraintValidatorContext;

public class HasLowerCaseValidatorImpl implements HasLowerCaseValidator {

    private final ValidatorRules validator = ValidatorRulesImpl.INSTANCE;
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) return true;
        return validator.hasLowerCase(value);
    }
}
