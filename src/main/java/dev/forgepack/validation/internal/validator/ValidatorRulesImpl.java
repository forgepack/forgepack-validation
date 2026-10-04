package dev.forgepack.validation.internal.validator;

import dev.forgepack.validation.api.validator.ValidatorRules;

public final class ValidatorRulesImpl implements ValidatorRules {

    public static final ValidatorRules INSTANCE = new ValidatorRulesImpl();

    private ValidatorRulesImpl() {}

    public boolean isNull(Object value) {
        return value == null;
    }

    public boolean hasDigit(String value) {
        return !isNull(value) && value.chars().anyMatch(Character::isDigit);
    }

    public boolean hasLetter(String value) {
        return !isNull(value) && value.chars().anyMatch(Character::isLetter);
    }

    public boolean hasLowerCase(String value) {
        return !isNull(value) && value.chars().anyMatch(Character::isLowerCase);
    }

    public boolean hasUpperCase(String value) {
        return !isNull(value) && value.chars().anyMatch(Character::isUpperCase);
    }

    public boolean hasLength(int length, String value) {
        return !isNull(value) && value.length() >= length;
    }
}
