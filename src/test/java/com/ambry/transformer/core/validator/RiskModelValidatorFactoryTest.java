package com.ambry.transformer.core.validator;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.objex.risk.beans.uno.Model.ModelName;

@DisplayName("RiskModelValidatorFactory Unit Tests")
class RiskModelValidatorFactoryTest {

    @Test
    @DisplayName("Should create factory with list of validators")
    void testConstructorWithValidators() {
        RiskModelValidator<?> validator1 = createMockValidator(ModelName.BRCAPRO);
        RiskModelValidator<?> validator2 = createMockValidator(ModelName.GAIL);
        List<RiskModelValidator<?>> validatorList = Arrays.asList(validator1, validator2);

        RiskModelValidatorFactory factory = new RiskModelValidatorFactory(validatorList);

        assertThat(factory).isNotNull();
    }

    @Test
    @DisplayName("Should return correct validator for known ModelName")
    void testGetValidatorForKnownModel() {
        RiskModelValidator<?> validator1 = createMockValidator(ModelName.BRCAPRO);
        RiskModelValidator<?> validator2 = createMockValidator(ModelName.GAIL);
        List<RiskModelValidator<?>> validatorList = Arrays.asList(validator1, validator2);
        RiskModelValidatorFactory factory = new RiskModelValidatorFactory(validatorList);

        RiskModelValidator<?> result = factory.getValidator(ModelName.BRCAPRO);

        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(validator1);
    }

    @Test
    @DisplayName("Should return null for unknown ModelName")
    void testGetValidatorForUnknownModel() {
        RiskModelValidator<?> validator1 = createMockValidator(ModelName.BRCAPRO);
        List<RiskModelValidator<?>> validatorList = Arrays.asList(validator1);
        RiskModelValidatorFactory factory = new RiskModelValidatorFactory(validatorList);

        RiskModelValidator<?> result = factory.getValidator(ModelName.GAIL);

        assertThat(result).isNull();
    }

    @Test
    @DisplayName("Should handle empty validator list")
    void testConstructorWithEmptyList() {
        List<RiskModelValidator<?>> emptyList = Arrays.asList();
        RiskModelValidatorFactory factory = new RiskModelValidatorFactory(emptyList);

        assertThat(factory).isNotNull();
        assertThat(factory.getValidator(ModelName.BRCAPRO)).isNull();
    }

    @Test
    @DisplayName("Should return correct validator for multiple models")
    void testGetValidatorForMultipleModels() {
        RiskModelValidator<?> validator1 = createMockValidator(ModelName.BRCAPRO);
        RiskModelValidator<?> validator2 = createMockValidator(ModelName.GAIL);
        RiskModelValidator<?> validator3 = createMockValidator(ModelName.MELAPRO);
        List<RiskModelValidator<?>> validatorList = Arrays.asList(validator1, validator2, validator3);
        RiskModelValidatorFactory factory = new RiskModelValidatorFactory(validatorList);

        assertThat(factory.getValidator(ModelName.BRCAPRO)).isEqualTo(validator1);
        assertThat(factory.getValidator(ModelName.GAIL)).isEqualTo(validator2);
        assertThat(factory.getValidator(ModelName.MELAPRO)).isEqualTo(validator3);
    }

    private RiskModelValidator<?> createMockValidator(ModelName modelName) {
        return new RiskModelValidator<Object>() {
            @Override
            public void validate(Object request, ValidationErrors validationErros) {
                // Mock implementation
            }

            @Override
            public ModelName getModelName() {
                return modelName;
            }
        };
    }
}

