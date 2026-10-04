package dev.forgepack.validation.internal.validator;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ValidatorRulesImplTest {

    private final ValidatorRulesImpl sut = (ValidatorRulesImpl) ValidatorRulesImpl.INSTANCE;

    // ─── isNull ───────────────────────────────────────────────────────────────

    @Test
    void isNull_returnsTrue_whenNull() {
        assertThat(sut.isNull(null)).isTrue();
    }

    @Test
    void isNull_returnsFalse_whenEmptyString() {
        assertThat(sut.isNull("")).isFalse();
    }

    @Test
    void isNull_returnsFalse_whenObject() {
        assertThat(sut.isNull(new Object())).isFalse();
    }

    // ─── hasDigit ─────────────────────────────────────────────────────────────

    @Test
    void hasDigit_returnsFalse_whenNull() {
        assertThat(sut.hasDigit(null)).isFalse();
    }

    @Test
    void hasDigit_returnsFalse_whenEmpty() {
        assertThat(sut.hasDigit("")).isFalse();
    }

    @Test
    void hasDigit_returnsFalse_whenOnlyLetters() {
        assertThat(sut.hasDigit("abcXYZ")).isFalse();
    }

    @Test
    void hasDigit_returnsFalse_whenOnlySpecialChars() {
        assertThat(sut.hasDigit("!@#$%")).isFalse();
    }

    @Test
    void hasDigit_returnsTrue_whenContainsDigit() {
        assertThat(sut.hasDigit("abc1xyz")).isTrue();
    }

    @Test
    void hasDigit_returnsTrue_whenAllDigits() {
        assertThat(sut.hasDigit("12345")).isTrue();
    }

    // ─── hasLetter ────────────────────────────────────────────────────────────

    @Test
    void hasLetter_returnsFalse_whenNull() {
        assertThat(sut.hasLetter(null)).isFalse();
    }

    @Test
    void hasLetter_returnsFalse_whenEmpty() {
        assertThat(sut.hasLetter("")).isFalse();
    }

    @Test
    void hasLetter_returnsFalse_whenOnlyDigits() {
        assertThat(sut.hasLetter("12345")).isFalse();
    }

    @Test
    void hasLetter_returnsFalse_whenOnlySpecialChars() {
        assertThat(sut.hasLetter("!@#$%")).isFalse();
    }

    @Test
    void hasLetter_returnsTrue_whenContainsLetter() {
        assertThat(sut.hasLetter("123a")).isTrue();
    }

    @Test
    void hasLetter_returnsTrue_whenOnlyLetters() {
        assertThat(sut.hasLetter("abcXYZ")).isTrue();
    }

    @Test
    void hasLetter_returnsTrue_withUnicodeLetter() {
        assertThat(sut.hasLetter("café")).isTrue();
    }

    // ─── hasLowerCase ─────────────────────────────────────────────────────────

    @Test
    void hasLowerCase_returnsFalse_whenNull() {
        assertThat(sut.hasLowerCase(null)).isFalse();
    }

    @Test
    void hasLowerCase_returnsFalse_whenEmpty() {
        assertThat(sut.hasLowerCase("")).isFalse();
    }

    @Test
    void hasLowerCase_returnsFalse_whenOnlyUpperCase() {
        assertThat(sut.hasLowerCase("ABC")).isFalse();
    }

    @Test
    void hasLowerCase_returnsFalse_whenOnlyDigits() {
        assertThat(sut.hasLowerCase("123")).isFalse();
    }

    @Test
    void hasLowerCase_returnsTrue_whenContainsLowerCase() {
        assertThat(sut.hasLowerCase("ABCd")).isTrue();
    }

    @Test
    void hasLowerCase_returnsTrue_whenAllLowerCase() {
        assertThat(sut.hasLowerCase("abc")).isTrue();
    }

    // ─── hasUpperCase ─────────────────────────────────────────────────────────

    @Test
    void hasUpperCase_returnsFalse_whenNull() {
        assertThat(sut.hasUpperCase(null)).isFalse();
    }

    @Test
    void hasUpperCase_returnsFalse_whenEmpty() {
        assertThat(sut.hasUpperCase("")).isFalse();
    }

    @Test
    void hasUpperCase_returnsFalse_whenOnlyLowerCase() {
        assertThat(sut.hasUpperCase("abc")).isFalse();
    }

    @Test
    void hasUpperCase_returnsFalse_whenOnlyDigits() {
        assertThat(sut.hasUpperCase("123")).isFalse();
    }

    @Test
    void hasUpperCase_returnsTrue_whenContainsUpperCase() {
        assertThat(sut.hasUpperCase("abcD")).isTrue();
    }

    @Test
    void hasUpperCase_returnsTrue_whenAllUpperCase() {
        assertThat(sut.hasUpperCase("ABC")).isTrue();
    }

    // ─── hasLength ────────────────────────────────────────────────────────────

    @Test
    void hasLength_returnsFalse_whenNull() {
        assertThat(sut.hasLength(4, null)).isFalse();
    }

    @Test
    void hasLength_returnsFalse_whenTooShort() {
        assertThat(sut.hasLength(8, "abc")).isFalse();
    }

    @Test
    void hasLength_returnsFalse_whenOneCharShort() {
        assertThat(sut.hasLength(4, "abc")).isFalse();
    }

    @Test
    void hasLength_returnsTrue_whenExactLength() {
        assertThat(sut.hasLength(3, "abc")).isTrue();
    }

    @Test
    void hasLength_returnsTrue_whenLongerThanMin() {
        assertThat(sut.hasLength(3, "abcdef")).isTrue();
    }

    @Test
    void hasLength_returnsTrue_whenZeroMinAndEmptyString() {
        assertThat(sut.hasLength(0, "")).isTrue();
    }
}
