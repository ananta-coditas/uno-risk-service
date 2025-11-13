package com.ambry.transformer.tyrer;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.ambry.transformer.core.validator.ValidationErrors;
import com.objex.risk.beans.tcuzick.Risk;
import com.objex.risk.beans.uno.Model.ModelName;

@DisplayName("TyrerValidator Unit Tests")
class TyrerValidatorTest {

    private TyrerValidator validator;

    @BeforeEach
    void setUp() {
        validator = new TyrerValidator();
    }

    @Test
    @DisplayName("Should return TYRER model name")
    void testGetModelName() {
        ModelName result = validator.getModelName();

        assertThat(result).isEqualTo(ModelName.TYRER);
    }

    @Test
    @DisplayName("Should validate without errors for valid risk")
    void testValidateWithValidRisk() {
        Risk tyrerRisk = new Risk();
        ValidationErrors validationErrors = new ValidationErrors();

        validator.validate(tyrerRisk, validationErrors);

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
        Risk tyrerRisk = new Risk();

        // Should not throw exception
        validator.validate(tyrerRisk, null);
    }
}

