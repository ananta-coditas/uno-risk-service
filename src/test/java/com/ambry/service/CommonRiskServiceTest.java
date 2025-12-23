package com.ambry.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import com.ambry.exception.ValidationException;
import com.ambry.transformer.core.RiskModelErrorDto;
import com.ambry.transformer.core.RiskModelExecuterFactory;
import com.ambry.transformer.core.RiskModelExecutor;
import com.ambry.transformer.core.validator.ValidationErrors;
import com.objex.risk.beans.BaseResult;
import com.objex.risk.beans.RiskError;
import com.objex.risk.beans.uno.CombinedResult;
import com.objex.risk.beans.uno.Model;
import com.objex.risk.beans.uno.Model.ModelName;
import com.objex.risk.beans.uno.Risk;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("CommonRiskService Unit Tests")
class CommonRiskServiceTest {

    @Mock
    private RiskModelExecuterFactory riskModelExecuterFactory;

    @InjectMocks
    private CommonRiskService commonRiskService;

    private Risk risk;
    private Model model;
    private RiskModelExecutor<?, ?, ? extends BaseResult> mockExecutor;
    private BaseResult mockResult;

    @BeforeEach
    void setUp() {
        risk = new Risk();
        model = new Model();
        model.setName(ModelName.GAIL);
        risk.getRequestedModels().add(model);

        mockExecutor = mock(RiskModelExecutor.class);
        mockResult = mock(BaseResult.class);
    }

    @Test
    @DisplayName("Should successfully calculate risk models with valid input")
    @SuppressWarnings({ "unchecked", "rawtypes" })
    void testCalculateRiskModelsSuccess() throws Exception {
        doReturn(mockExecutor).when(riskModelExecuterFactory).getRiskModelExecuter(ModelName.GAIL);
        doAnswer(invocation -> mockResult).when((RiskModelExecutor) mockExecutor).execute(any(Risk.class), any(Model.class));

        CombinedResult result = commonRiskService.calculateRiskModels(risk);

        assertNotNull(result);
        verify((RiskModelExecutor<?, ?, ?>) mockExecutor).execute(any(Risk.class), eq(model));
    }

    @Test
    @DisplayName("Should handle ValidationException and add error to result")
    @SuppressWarnings({ "unchecked", "rawtypes" })
    void testCalculateRiskModelsWithValidationException() throws Exception {
        ValidationErrors validationErrors = new ValidationErrors();
        RiskError error = new RiskError(null, "VALIDATION_ERROR", "Invalid input data");
        validationErrors.getErrors().add(error);
        ValidationException validationException = new ValidationException("Validation failed", validationErrors);

        doReturn(mockExecutor).when(riskModelExecuterFactory).getRiskModelExecuter(ModelName.GAIL);
        doThrow(validationException).when((RiskModelExecutor) mockExecutor).execute(any(Risk.class), any(Model.class));

        CombinedResult result = commonRiskService.calculateRiskModels(risk);

        assertNotNull(result);
        assertNotNull(result.getResults().get(ModelName.GAIL));
        RiskModelErrorDto errorDto = (RiskModelErrorDto) result.getResults().get(ModelName.GAIL);
        assertEquals(1, errorDto.getErrors().size());
        assertEquals("VALIDATION_ERROR", errorDto.getErrors().get(0).getCode());
    }

    @Test
    @DisplayName("Should handle general Exception and add error to result")
    @SuppressWarnings({ "unchecked", "rawtypes" })
    void testCalculateRiskModelsWithGeneralException() throws Exception {
        RuntimeException runtimeException = new RuntimeException("Unexpected error occurred");

        doReturn(mockExecutor).when(riskModelExecuterFactory).getRiskModelExecuter(ModelName.GAIL);
        doThrow(runtimeException).when((RiskModelExecutor) mockExecutor).execute(any(Risk.class), any(Model.class));

        CombinedResult result = commonRiskService.calculateRiskModels(risk);

        assertNotNull(result);
        assertNotNull(result.getResults().get(ModelName.GAIL));
        RiskModelErrorDto errorDto = (RiskModelErrorDto) result.getResults().get(ModelName.GAIL);
        assertEquals(1, errorDto.getErrors().size());
        RiskError error = errorDto.getErrors().get(0);
        assertNotNull(error);
        // The error message should be in either code or description field
        String errorMessage = (error.getDescription() != null && !error.getDescription().isEmpty()) 
            ? error.getDescription() 
            : (error.getCode() != null ? error.getCode() : "");
        // Verify that the error contains the expected message in either field
        assertTrue(errorMessage.contains("Unexpected error occurred") || 
                   (error.getCode() != null && error.getCode().contains("Unexpected error occurred")) ||
                   (error.getDescription() != null && error.getDescription().contains("Unexpected error occurred")));
    }

    @Test
    @DisplayName("Should handle ValidationException with empty validation errors")
    @SuppressWarnings({ "unchecked", "rawtypes" })
    void testCalculateRiskModelsWithValidationExceptionEmptyErrors() throws Exception {
        ValidationErrors validationErrors = new ValidationErrors();
        ValidationException validationException = new ValidationException("Validation failed", validationErrors);

        doReturn(mockExecutor).when(riskModelExecuterFactory).getRiskModelExecuter(ModelName.GAIL);
        doThrow(validationException).when((RiskModelExecutor) mockExecutor).execute(any(Risk.class), any(Model.class));

        CombinedResult result = commonRiskService.calculateRiskModels(risk);

        assertNotNull(result);
        assertNotNull(result.getResults().get(ModelName.GAIL));
        RiskModelErrorDto errorDto = (RiskModelErrorDto) result.getResults().get(ModelName.GAIL);
        assertEquals(1, errorDto.getErrors().size());
        RiskError error = errorDto.getErrors().get(0);
        assertNotNull(error);
        // When validation errors are empty, the exception message should be used
        // The message should be in either code or description field
        assertTrue((error.getDescription() != null && error.getDescription().contains("Validation failed")) ||
                   (error.getCode() != null && error.getCode().contains("Validation failed")));
    }

    @Test
    @DisplayName("Should handle multiple models in parallel")
    @SuppressWarnings({ "unchecked", "rawtypes" })
    void testCalculateRiskModelsMultipleModels() throws Exception {
        Model model2 = new Model();
        model2.setName(ModelName.BRCAPRO);
        risk.getRequestedModels().add(model2);

        @SuppressWarnings("unchecked")
        RiskModelExecutor<?, ?, ? extends BaseResult> mockExecutor2 = mock(RiskModelExecutor.class);
        BaseResult mockResult2 = mock(BaseResult.class);

        doReturn(mockExecutor).when(riskModelExecuterFactory).getRiskModelExecuter(ModelName.GAIL);
        doReturn(mockExecutor2).when(riskModelExecuterFactory).getRiskModelExecuter(ModelName.BRCAPRO);
        doAnswer(invocation -> mockResult).when((RiskModelExecutor) mockExecutor).execute(any(Risk.class), any(Model.class));
        doAnswer(invocation -> mockResult2).when((RiskModelExecutor) mockExecutor2).execute(any(Risk.class), any(Model.class));

        CombinedResult result = commonRiskService.calculateRiskModels(risk);

        assertNotNull(result);
        assertEquals(2, result.getResults().size());
        verify((RiskModelExecutor<?, ?, ?>) mockExecutor).execute(any(Risk.class), eq(model));
        verify((RiskModelExecutor<?, ?, ?>) mockExecutor2).execute(any(Risk.class), eq(model2));
    }

    @Test
    @DisplayName("Should handle empty requested models list")
    void testCalculateRiskModelsEmptyModelsList() {
        risk.getRequestedModels().clear();

        CombinedResult result = commonRiskService.calculateRiskModels(risk);

        assertNotNull(result);
        assertEquals(0, result.getResults().size());
    }

    @Test
    @DisplayName("Should handle ValidationException with multiple validation errors")
    @SuppressWarnings({ "unchecked", "rawtypes" })
    void testCalculateRiskModelsWithMultipleValidationErrors() throws Exception {
        ValidationErrors validationErrors = new ValidationErrors();
        RiskError error1 = new RiskError(null, "ERROR_1", "First validation error");
        RiskError error2 = new RiskError(null, "ERROR_2", "Second validation error");
        RiskError error3 = new RiskError(null, "ERROR_3", "Third validation error");
        validationErrors.getErrors().add(error1);
        validationErrors.getErrors().add(error2);
        validationErrors.getErrors().add(error3);
        ValidationException validationException = new ValidationException("Multiple validation errors", validationErrors);

        doReturn(mockExecutor).when(riskModelExecuterFactory).getRiskModelExecuter(ModelName.GAIL);
        doThrow(validationException).when((RiskModelExecutor) mockExecutor).execute(any(Risk.class), any(Model.class));

        CombinedResult result = commonRiskService.calculateRiskModels(risk);

        assertNotNull(result);
        assertNotNull(result.getResults().get(ModelName.GAIL));
        RiskModelErrorDto errorDto = (RiskModelErrorDto) result.getResults().get(ModelName.GAIL);
        assertEquals(3, errorDto.getErrors().size());
        assertEquals("ERROR_1", errorDto.getErrors().get(0).getCode());
        assertEquals("ERROR_2", errorDto.getErrors().get(1).getCode());
        assertEquals("ERROR_3", errorDto.getErrors().get(2).getCode());
    }

    @Test
    @DisplayName("Should handle Exception with null message")
    @SuppressWarnings({ "unchecked", "rawtypes" })
    void testCalculateRiskModelsWithNullMessageException() throws Exception {
        RuntimeException runtimeException = new RuntimeException((String) null);

        doReturn(mockExecutor).when(riskModelExecuterFactory).getRiskModelExecuter(ModelName.GAIL);
        doThrow(runtimeException).when((RiskModelExecutor) mockExecutor).execute(any(Risk.class), any(Model.class));

        CombinedResult result = commonRiskService.calculateRiskModels(risk);

        assertNotNull(result);
        assertNotNull(result.getResults().get(ModelName.GAIL));
        RiskModelErrorDto errorDto = (RiskModelErrorDto) result.getResults().get(ModelName.GAIL);
        assertEquals(1, errorDto.getErrors().size());
        // When message is null, it should still create an error
        RiskError error = errorDto.getErrors().get(0);
        assertNotNull(error);
        // When message is null, error should still be created
        // The description or code may be null or empty, which is expected
    }

    @Test
    @DisplayName("Should handle mixed success and failure scenarios")
    @SuppressWarnings({ "unchecked", "rawtypes" })
    void testCalculateRiskModelsMixedSuccessAndFailure() throws Exception {
        Model model2 = new Model();
        model2.setName(ModelName.BRCAPRO);
        risk.getRequestedModels().add(model2);

        ValidationErrors validationErrors = new ValidationErrors();
        RiskError error = new RiskError(null, "VALIDATION_ERROR", "Validation failed");
        validationErrors.getErrors().add(error);
        ValidationException validationException = new ValidationException("Validation failed", validationErrors);

        @SuppressWarnings("unchecked")
        RiskModelExecutor<?, ?, ? extends BaseResult> mockExecutor2 = mock(RiskModelExecutor.class);
        BaseResult mockResult2 = mock(BaseResult.class);

        doReturn(mockExecutor).when(riskModelExecuterFactory).getRiskModelExecuter(ModelName.GAIL);
        doReturn(mockExecutor2).when(riskModelExecuterFactory).getRiskModelExecuter(ModelName.BRCAPRO);
        doAnswer(invocation -> mockResult).when((RiskModelExecutor) mockExecutor).execute(any(Risk.class), any(Model.class));
        doThrow(validationException).when((RiskModelExecutor) mockExecutor2).execute(any(Risk.class), any(Model.class));

        CombinedResult result = commonRiskService.calculateRiskModels(risk);

        assertNotNull(result);
        assertEquals(2, result.getResults().size());
        // GAIL should succeed
        assertNotNull(result.getResults().get(ModelName.GAIL));
        // BRCAPRO should have error
        assertNotNull(result.getResults().get(ModelName.BRCAPRO));
        RiskModelErrorDto errorDto = (RiskModelErrorDto) result.getResults().get(ModelName.BRCAPRO);
        assertEquals(1, errorDto.getErrors().size());
        assertEquals("VALIDATION_ERROR", errorDto.getErrors().get(0).getCode());
    }

    @Test
    @DisplayName("Should handle multiple models with different exception types")
    @SuppressWarnings({ "unchecked", "rawtypes" })
    void testCalculateRiskModelsWithDifferentExceptionTypes() throws Exception {
        Model model2 = new Model();
        model2.setName(ModelName.BRCAPRO);
        Model model3 = new Model();
        model3.setName(ModelName.CLAUS);
        risk.getRequestedModels().add(model2);
        risk.getRequestedModels().add(model3);

        ValidationErrors validationErrors = new ValidationErrors();
        RiskError error = new RiskError(null, "VALIDATION_ERROR", "Validation failed");
        validationErrors.getErrors().add(error);
        ValidationException validationException = new ValidationException("Validation failed", validationErrors);
        RuntimeException runtimeException = new RuntimeException("Runtime error occurred");

        @SuppressWarnings("unchecked")
        RiskModelExecutor<?, ?, ? extends BaseResult> mockExecutor2 = mock(RiskModelExecutor.class);
        @SuppressWarnings("unchecked")
        RiskModelExecutor<?, ?, ? extends BaseResult> mockExecutor3 = mock(RiskModelExecutor.class);
        BaseResult mockResult2 = mock(BaseResult.class);

        doReturn(mockExecutor).when(riskModelExecuterFactory).getRiskModelExecuter(ModelName.GAIL);
        doReturn(mockExecutor2).when(riskModelExecuterFactory).getRiskModelExecuter(ModelName.BRCAPRO);
        doReturn(mockExecutor3).when(riskModelExecuterFactory).getRiskModelExecuter(ModelName.CLAUS);
        doAnswer(invocation -> mockResult).when((RiskModelExecutor) mockExecutor).execute(any(Risk.class), any(Model.class));
        doThrow(validationException).when((RiskModelExecutor) mockExecutor2).execute(any(Risk.class), any(Model.class));
        doThrow(runtimeException).when((RiskModelExecutor) mockExecutor3).execute(any(Risk.class), any(Model.class));

        CombinedResult result = commonRiskService.calculateRiskModels(risk);

        assertNotNull(result);
        assertEquals(3, result.getResults().size());
        // GAIL should succeed
        assertNotNull(result.getResults().get(ModelName.GAIL));
        // BRCAPRO should have ValidationException error
        RiskModelErrorDto validationErrorDto = (RiskModelErrorDto) result.getResults().get(ModelName.BRCAPRO);
        assertEquals(1, validationErrorDto.getErrors().size());
        assertEquals("VALIDATION_ERROR", validationErrorDto.getErrors().get(0).getCode());
        // CLAUS should have RuntimeException error
        RiskModelErrorDto runtimeErrorDto = (RiskModelErrorDto) result.getResults().get(ModelName.CLAUS);
        assertEquals(1, runtimeErrorDto.getErrors().size());
        RiskError runtimeError = runtimeErrorDto.getErrors().get(0);
        assertNotNull(runtimeError);
        // The error message should be in either code or description field
        assertTrue((runtimeError.getDescription() != null && runtimeError.getDescription().contains("Runtime error occurred")) ||
                   (runtimeError.getCode() != null && runtimeError.getCode().contains("Runtime error occurred")));
    }

    @Test
    @DisplayName("Should handle ValidationException with null message and empty errors")
    @SuppressWarnings({ "unchecked", "rawtypes" })
    void testCalculateRiskModelsWithValidationExceptionNullMessage() throws Exception {
        ValidationErrors validationErrors = new ValidationErrors();
        ValidationException validationException = new ValidationException(validationErrors);

        doReturn(mockExecutor).when(riskModelExecuterFactory).getRiskModelExecuter(ModelName.GAIL);
        doThrow(validationException).when((RiskModelExecutor) mockExecutor).execute(any(Risk.class), any(Model.class));

        CombinedResult result = commonRiskService.calculateRiskModels(risk);

        assertNotNull(result);
        assertNotNull(result.getResults().get(ModelName.GAIL));
        RiskModelErrorDto errorDto = (RiskModelErrorDto) result.getResults().get(ModelName.GAIL);
        assertEquals(1, errorDto.getErrors().size());
        // When message is null, it should still create an error with empty code
        assertNotNull(errorDto.getErrors().get(0));
    }

    @Test
    @DisplayName("Should handle IllegalArgumentException")
    @SuppressWarnings({ "unchecked", "rawtypes" })
    void testCalculateRiskModelsWithIllegalArgumentException() throws Exception {
        IllegalArgumentException illegalArgumentException = new IllegalArgumentException("Invalid argument");

        doReturn(mockExecutor).when(riskModelExecuterFactory).getRiskModelExecuter(ModelName.GAIL);
        doThrow(illegalArgumentException).when((RiskModelExecutor) mockExecutor).execute(any(Risk.class), any(Model.class));

        CombinedResult result = commonRiskService.calculateRiskModels(risk);

        assertNotNull(result);
        assertNotNull(result.getResults().get(ModelName.GAIL));
        RiskModelErrorDto errorDto = (RiskModelErrorDto) result.getResults().get(ModelName.GAIL);
        assertEquals(1, errorDto.getErrors().size());
        RiskError error = errorDto.getErrors().get(0);
        assertNotNull(error);
        // The error message should be in either code or description field
        assertTrue((error.getDescription() != null && error.getDescription().contains("Invalid argument")) ||
                   (error.getCode() != null && error.getCode().contains("Invalid argument")));
    }

    @Test
    @DisplayName("Should handle multiple models where all fail with different exceptions")
    @SuppressWarnings({ "unchecked", "rawtypes" })
    void testCalculateRiskModelsAllFailWithDifferentExceptions() throws Exception {
        Model model2 = new Model();
        model2.setName(ModelName.BRCAPRO);
        risk.getRequestedModels().add(model2);

        ValidationErrors validationErrors = new ValidationErrors();
        RiskError error = new RiskError(null, "VALIDATION_ERROR", "Validation failed");
        validationErrors.getErrors().add(error);
        ValidationException validationException = new ValidationException("Validation failed", validationErrors);
        NullPointerException nullPointerException = new NullPointerException("Null pointer exception");

        @SuppressWarnings("unchecked")
        RiskModelExecutor<?, ?, ? extends BaseResult> mockExecutor2 = mock(RiskModelExecutor.class);

        doReturn(mockExecutor).when(riskModelExecuterFactory).getRiskModelExecuter(ModelName.GAIL);
        doReturn(mockExecutor2).when(riskModelExecuterFactory).getRiskModelExecuter(ModelName.BRCAPRO);
        doThrow(validationException).when((RiskModelExecutor) mockExecutor).execute(any(Risk.class), any(Model.class));
        doThrow(nullPointerException).when((RiskModelExecutor) mockExecutor2).execute(any(Risk.class), any(Model.class));

        CombinedResult result = commonRiskService.calculateRiskModels(risk);

        assertNotNull(result);
        assertEquals(2, result.getResults().size());
        // Both should have errors
        RiskModelErrorDto errorDto1 = (RiskModelErrorDto) result.getResults().get(ModelName.GAIL);
        assertEquals(1, errorDto1.getErrors().size());
        assertEquals("VALIDATION_ERROR", errorDto1.getErrors().get(0).getCode());
        
        RiskModelErrorDto errorDto2 = (RiskModelErrorDto) result.getResults().get(ModelName.BRCAPRO);
        assertEquals(1, errorDto2.getErrors().size());
        RiskError error2 = errorDto2.getErrors().get(0);
        assertNotNull(error2);
        // The error message should be in either code or description field
        assertTrue((error2.getDescription() != null && error2.getDescription().contains("Null pointer exception")) ||
                   (error2.getCode() != null && error2.getCode().contains("Null pointer exception")));
    }
}

