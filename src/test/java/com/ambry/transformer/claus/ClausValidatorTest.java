package com.ambry.transformer.claus;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.ambry.transformer.core.validator.ValidationErrors;
import com.objex.risk.beans.claus.Risk;
import com.objex.risk.beans.uno.Model.ModelName;

@DisplayName("ClausValidator Unit Tests")
class ClausValidatorTest {

    private ClausValidator validator;

    @BeforeEach
    void setUp() {
        validator = new ClausValidator();
    }

    @Test
    @DisplayName("Should return CLAUS model name")
    void testGetModelName() {
        ModelName result = validator.getModelName();

        assertThat(result).isEqualTo(ModelName.CLAUS);
    }

    @Test
    @DisplayName("Should validate without errors for valid risk")
    void testValidateWithValidRisk() {
        Risk clausRisk = new Risk();
        ValidationErrors validationErrors = new ValidationErrors();

        validator.validate(clausRisk, validationErrors);

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
        Risk clausRisk = new Risk();

        // Should not throw exception
        validator.validate(clausRisk, null);
    }
}

