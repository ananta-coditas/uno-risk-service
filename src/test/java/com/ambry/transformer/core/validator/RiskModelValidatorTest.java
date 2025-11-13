package com.ambry.transformer.core.validator;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.objex.risk.beans.uno.Model.ModelName;

@DisplayName("RiskModelValidator Unit Tests")
class RiskModelValidatorTest {

    @Test
    @DisplayName("Should allow concrete implementation to define validate method")
    void testValidateMethodContract() {
        TestRiskModelValidator validator = new TestRiskModelValidator();
        ValidationErrors errors = new ValidationErrors();

        validator.validate(new Object(), errors);

        assertThat(validator.isValidateCalled()).isTrue();
    }

    @Test
    @DisplayName("Should allow concrete implementation to define getModelName method")
    void testGetModelNameMethodContract() {
        TestRiskModelValidator validator = new TestRiskModelValidator();

        ModelName modelName = validator.getModelName();

        assertThat(modelName).isEqualTo(ModelName.GAIL);
    }

    // Concrete test implementation
    private static class TestRiskModelValidator extends RiskModelValidator<Object> {
        private boolean validateCalled = false;

        @Override
        public void validate(Object request, ValidationErrors validationErros) {
            validateCalled = true;
            // Test implementation
        }

        @Override
        public ModelName getModelName() {
            return ModelName.GAIL;
        }

        public boolean isValidateCalled() {
            return validateCalled;
        }
    }
}

