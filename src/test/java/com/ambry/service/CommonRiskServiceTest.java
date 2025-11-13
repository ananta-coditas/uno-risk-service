package com.ambry.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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

    //@Test
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
        assertEquals("Unexpected error occurred", errorDto.getErrors().get(0).getDescription());
    }

    //@Test
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
        assertEquals("Validation failed", errorDto.getErrors().get(0).getDescription());
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
}

