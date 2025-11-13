package com.ambry.transformer.mmr;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.ambry.transformer.core.validator.ValidationErrors;
import com.objex.risk.beans.mmr.Risk;
import com.objex.risk.beans.uno.Model.ModelName;

@DisplayName("MMRProValidator Unit Tests")
class MMRProValidatorTest {

    private MMRProValidator validator;

    @BeforeEach
    void setUp() {
        validator = new MMRProValidator();
    }

    @Test
    @DisplayName("Should return MMRPRO model name")
    void testGetModelName() {
        ModelName result = validator.getModelName();

        assertThat(result).isEqualTo(ModelName.MMRPRO);
    }

    @Test
    @DisplayName("Should validate without errors for valid risk")
    void testValidateWithValidRisk() {
        Risk mmrRisk = new Risk();
        ValidationErrors validationErrors = new ValidationErrors();

        validator.validate(mmrRisk, validationErrors);

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
        Risk mmrRisk = new Risk();

        // Should not throw exception
        validator.validate(mmrRisk, null);
    }
}

