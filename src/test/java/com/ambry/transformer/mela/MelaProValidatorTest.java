package com.ambry.transformer.mela;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.ambry.transformer.core.validator.ValidationErrors;
import com.objex.risk.beans.mela.Risk;
import com.objex.risk.beans.uno.Model.ModelName;

@DisplayName("MelaProValidator Unit Tests")
class MelaProValidatorTest {

    private MelaProValidator validator;

    @BeforeEach
    void setUp() {
        validator = new MelaProValidator();
    }

    @Test
    @DisplayName("Should return MELAPRO model name")
    void testGetModelName() {
        ModelName result = validator.getModelName();

        assertThat(result).isEqualTo(ModelName.MELAPRO);
    }

    @Test
    @DisplayName("Should validate without errors for valid risk")
    void testValidateWithValidRisk() {
        Risk melaRisk = new Risk();
        ValidationErrors validationErrors = new ValidationErrors();

        validator.validate(melaRisk, validationErrors);

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
        Risk melaRisk = new Risk();

        // Should not throw exception
        validator.validate(melaRisk, null);
    }
}

