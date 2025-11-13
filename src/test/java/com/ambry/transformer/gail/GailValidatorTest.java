package com.ambry.transformer.gail;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.ambry.transformer.core.validator.ValidationErrors;
import com.objex.risk.beans.gail.Risk;
import com.objex.risk.beans.uno.Model.ModelName;

@DisplayName("GailValidator Unit Tests")
class GailValidatorTest {

    private GailValidator validator;

    @BeforeEach
    void setUp() {
        validator = new GailValidator();
    }

    @Test
    @DisplayName("Should return GAIL model name")
    void testGetModelName() {
        ModelName result = validator.getModelName();

        assertThat(result).isEqualTo(ModelName.GAIL);
    }

    @Test
    @DisplayName("Should validate without errors for valid risk")
    void testValidateWithValidRisk() {
        Risk gailRisk = new Risk();
        ValidationErrors validationErrors = new ValidationErrors();

        validator.validate(gailRisk, validationErrors);

        assertThat(validationErrors.hasErros()).isFalse();
    }

    @Test
    @DisplayName("Should validate with null risk")
    void testValidateWithNullRisk() {
        ValidationErrors validationErrors = new ValidationErrors();

        validator.validate(null, validationErrors);

        assertThat(validationErrors.hasErros()).isFalse();
    }

    @Test
    @DisplayName("Should validate with null validation errors")
    void testValidateWithNullValidationErrors() {
        Risk gailRisk = new Risk();

        // Should not throw exception
        validator.validate(gailRisk, null);
    }
}

