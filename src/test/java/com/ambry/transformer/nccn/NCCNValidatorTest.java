package com.ambry.transformer.nccn;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.ambry.transformer.core.validator.ValidationErrors;
import com.genomebrain.backend.service.dto.NCCNCoreRequest;
import com.objex.risk.beans.uno.Model.ModelName;

@DisplayName("NCCNValidator Unit Tests")
class NCCNValidatorTest {

    private NCCNValidator validator;

    @BeforeEach
    void setUp() {
        validator = new NCCNValidator();
    }

    @Test
    @DisplayName("Should return NCCN model name")
    void testGetModelName() {
        ModelName result = validator.getModelName();

        assertThat(result).isEqualTo(ModelName.NCCN);
    }

    @Test
    @DisplayName("Should validate without errors for valid request")
    void testValidateWithValidRequest() {
        NCCNCoreRequest request = new NCCNCoreRequest();
        ValidationErrors validationErrors = new ValidationErrors();

        validator.validate(request, validationErrors);

        assertThat(validationErrors.hasErros()).isFalse();
    }

    @Test
    @DisplayName("Should validate with null request")
    void testValidateWithNullRequest() {
        ValidationErrors validationErrors = new ValidationErrors();

        validator.validate(null, validationErrors);

        assertThat(validationErrors.hasErros()).isFalse();
    }

    @Test
    @DisplayName("Should validate with null validation errors")
    void testValidateWithNullValidationErrors() {
        NCCNCoreRequest request = new NCCNCoreRequest();

        // Should not throw exception
        validator.validate(request, null);
    }
}

