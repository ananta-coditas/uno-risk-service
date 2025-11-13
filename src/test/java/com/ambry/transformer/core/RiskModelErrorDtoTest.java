package com.ambry.transformer.core;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("RiskModelErrorDto Unit Tests")
class RiskModelErrorDtoTest {

    @Test
    @DisplayName("Should return null for getModel")
    void testGetModel() {
        RiskModelErrorDto errorDto = new RiskModelErrorDto();

        String result = errorDto.getModel();

        assertThat(result).isNull();
    }

    @Test
    @DisplayName("Should return null for getColumnToFields")
    void testGetColumnToFields() {
        RiskModelErrorDto errorDto = new RiskModelErrorDto();

        java.util.Map<String, String> result = errorDto.getColumnToFields();

        assertThat(result).isNull();
    }

    @Test
    @DisplayName("Should be instance of BaseResult")
    void testIsInstanceOfBaseResult() {
        RiskModelErrorDto errorDto = new RiskModelErrorDto();

        assertThat(errorDto).isInstanceOf(com.objex.risk.beans.BaseResult.class);
    }
}

