package com.ambry.transformer.premm;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.ambry.transformer.core.validator.ValidationErrors;
import com.objex.risk.beans.premm.Risk;
import com.objex.risk.beans.uno.Model.ModelName;

@DisplayName("PremmValidator Unit Tests")
class PremmValidatorTest {

    private PremmValidator validator;

    @BeforeEach
    void setUp() {
        validator = new PremmValidator();
    }

    @Test
    @DisplayName("Should return PREMM model name")
    void testGetModelName() {
        ModelName result = validator.getModelName();

        assertThat(result).isEqualTo(ModelName.PREMM);
    }

    @Test
    @DisplayName("Should validate without errors for valid risk")
    void testValidateWithValidRisk() {
        Risk premmRisk = new Risk();
        ValidationErrors validationErrors = new ValidationErrors();

        validator.validate(premmRisk, validationErrors);

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
        Risk premmRisk = new Risk();

        // Should not throw exception
        validator.validate(premmRisk, null);
    }
}

