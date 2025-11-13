package com.ambry.transformer.panc;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.ambry.transformer.core.validator.ValidationErrors;
import com.objex.risk.beans.panc.Risk;
import com.objex.risk.beans.uno.Model.ModelName;

@DisplayName("PancProValidator Unit Tests")
class PancProValidatorTest {

    private PancProValidator validator;

    @BeforeEach
    void setUp() {
        validator = new PancProValidator();
    }

    @Test
    @DisplayName("Should return PANCPRO model name")
    void testGetModelName() {
        ModelName result = validator.getModelName();

        assertThat(result).isEqualTo(ModelName.PANCPRO);
    }

    @Test
    @DisplayName("Should validate without errors for valid risk")
    void testValidateWithValidRisk() {
        Risk pancRisk = new Risk();
        ValidationErrors validationErrors = new ValidationErrors();

        validator.validate(pancRisk, validationErrors);

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
        Risk pancRisk = new Risk();

        // Should not throw exception
        validator.validate(pancRisk, null);
    }
}

