package com.ambry.transformer.brca;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.ambry.transformer.core.validator.ValidationErrors;
import com.objex.risk.beans.brca.Risk;
import com.objex.risk.beans.uno.Model.ModelName;

@DisplayName("BRCAProValidator Unit Tests")
class BRCAProValidatorTest {

    private BRCAProValidator validator;

    @BeforeEach
    void setUp() {
        validator = new BRCAProValidator();
    }

    @Test
    @DisplayName("Should return BRCAPRO model name")
    void testGetModelName() {
        ModelName result = validator.getModelName();

        assertThat(result).isEqualTo(ModelName.BRCAPRO);
    }

    @Test
    @DisplayName("Should validate without errors for valid risk")
    void testValidateWithValidRisk() {
        Risk brcaRisk = new Risk();
        ValidationErrors validationErrors = new ValidationErrors();

        validator.validate(brcaRisk, validationErrors);

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
        Risk brcaRisk = new Risk();

        // Should not throw exception
        validator.validate(brcaRisk, null);
    }
}

