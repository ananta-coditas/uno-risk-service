package com.ambry.transformer.core;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.ambry.exception.RiskModelProcessingException;
import com.ambry.exception.ValidationException;
import com.ambry.transformer.core.validator.RiskModelValidator;
import com.ambry.transformer.core.validator.ValidationErrors;
import com.objex.risk.beans.BaseResult;
import com.objex.risk.beans.uno.Model;
import com.objex.risk.beans.uno.Model.ModelName;
import com.objex.risk.beans.uno.Risk;

@DisplayName("RiskModelExecutor Unit Tests")
class RiskModelExecutorTest {

    private TestRiskModelExecutor executor;
    private RiskModelValidator<Object> mockValidator;
    private Object mockRequest;
    private Object mockModelResponse;
    private BaseResult mockResult;
    private Risk mockRisk;
    private Model mockModel;

    @BeforeEach
    void setUp() {
        executor = new TestRiskModelExecutor();
        mockValidator = mock(RiskModelValidator.class);
        mockRequest = new Object();
        mockModelResponse = new Object();
        mockResult = mock(BaseResult.class);
        mockRisk = mock(Risk.class);
        mockModel = mock(Model.class);
        
        executor.setValidator(mockValidator);
        executor.setRequest(mockRequest);
        executor.setModelResponse(mockModelResponse);
        executor.setResult(mockResult);
    }

    @Test
    @DisplayName("Should execute successfully with valid request and no validation errors")
    void testExecuteSuccess() throws Exception {
        org.mockito.Mockito.doAnswer(invocation -> {
            // Do nothing - no errors
            return null;
        }).when(mockValidator).validate(any(), any());

        BaseResult result = executor.execute(mockRisk, mockModel);

        assertThat(result).isEqualTo(mockResult);
    }

    @Test
    @DisplayName("Should re-throw ValidationException when validation fails")
    void testExecuteWithValidationException() {
        org.mockito.Mockito.doAnswer(invocation -> {
            ValidationErrors errors = invocation.getArgument(1);
            errors.rejectValue("field", "Error message");
            return null;
        }).when(mockValidator).validate(any(), any());

        assertThatThrownBy(() -> executor.execute(mockRisk, mockModel))
            .isInstanceOf(ValidationException.class)
            .hasMessageContaining("Error while risk model execution");
    }

    @Test
    @DisplayName("Should wrap exceptions in RiskModelProcessingException")
    void testExecuteWithException() {
        RuntimeException testException = new RuntimeException("Test exception");
        executor.setExceptionToThrow(testException);

        assertThatThrownBy(() -> executor.execute(mockRisk, mockModel))
            .isInstanceOf(RiskModelProcessingException.class)
            .hasMessageContaining("Error while calculation of risk model")
            .hasCause(testException);
    }

    @Test
    @DisplayName("Should handle null validator")
    void testExecuteWithNullValidator() throws Exception {
        executor.setValidator(null);

        BaseResult result = executor.execute(mockRisk, mockModel);

        assertThat(result).isEqualTo(mockResult);
    }

    @Test
    @DisplayName("Should return correct model name")
    void testGetModelName() {
        assertThat(executor.getModelName()).isEqualTo(ModelName.GAIL);
    }

    // Test implementation of RiskModelExecutor
    private static class TestRiskModelExecutor implements RiskModelExecutor<Object, Object, BaseResult> {
        private RiskModelValidator<Object> validator;
        private Object request;
        private Object modelResponse;
        private BaseResult result;
        private RuntimeException exceptionToThrow;

        public void setValidator(RiskModelValidator<Object> validator) {
            this.validator = validator;
        }

        public void setRequest(Object request) {
            this.request = request;
        }

        public void setModelResponse(Object modelResponse) {
            this.modelResponse = modelResponse;
        }

        public void setResult(BaseResult result) {
            this.result = result;
        }

        public void setExceptionToThrow(RuntimeException exceptionToThrow) {
            this.exceptionToThrow = exceptionToThrow;
        }

        @Override
        public Object transformRequest(Risk commonRequest, Model model) {
            if (exceptionToThrow != null && exceptionToThrow.getClass() == RuntimeException.class) {
                throw exceptionToThrow;
            }
            return request;
        }

        @Override
        public Object invokeRiskModel(Object formattedRequest) {
            if (exceptionToThrow != null) {
                throw exceptionToThrow;
            }
            return modelResponse;
        }

        @Override
        public BaseResult transformResponse(Object modelResponse) {
            return result;
        }

        @Override
        public ModelName getModelName() {
            return ModelName.GAIL;
        }

        @Override
        public RiskModelValidator<Object> getRiskModelValidator() {
            return validator;
        }
    }
}

