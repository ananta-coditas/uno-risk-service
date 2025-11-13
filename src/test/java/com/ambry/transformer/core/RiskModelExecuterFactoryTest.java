package com.ambry.transformer.core;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.objex.risk.beans.BaseResult;
import com.objex.risk.beans.uno.Model.ModelName;

@DisplayName("RiskModelExecuterFactory Unit Tests")
class RiskModelExecuterFactoryTest {

    @Test
    @DisplayName("Should create factory with list of executors")
    void testConstructorWithExecutors() {
        RiskModelExecutor<?, ?, ? extends BaseResult> executor1 = createMockExecutor(ModelName.BRCAPRO);
        RiskModelExecutor<?, ?, ? extends BaseResult> executor2 = createMockExecutor(ModelName.GAIL);
        List<RiskModelExecutor<?, ?, ?>> executorList = Arrays.asList(executor1, executor2);

        RiskModelExecuterFactory factory = new RiskModelExecuterFactory(executorList);

        assertThat(factory).isNotNull();
    }

    @Test
    @DisplayName("Should return correct executor for known ModelName")
    void testGetRiskModelExecuterForKnownModel() {
        RiskModelExecutor<?, ?, ? extends BaseResult> executor1 = createMockExecutor(ModelName.BRCAPRO);
        RiskModelExecutor<?, ?, ? extends BaseResult> executor2 = createMockExecutor(ModelName.GAIL);
        List<RiskModelExecutor<?, ?, ?>> executorList = Arrays.asList(executor1, executor2);
        RiskModelExecuterFactory factory = new RiskModelExecuterFactory(executorList);

        RiskModelExecutor<?, ?, ? extends BaseResult> result = factory.getRiskModelExecuter(ModelName.BRCAPRO);

        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(executor1);
    }

    @Test
    @DisplayName("Should return null for unknown ModelName")
    void testGetRiskModelExecuterForUnknownModel() {
        RiskModelExecutor<?, ?, ? extends BaseResult> executor1 = createMockExecutor(ModelName.BRCAPRO);
        List<RiskModelExecutor<?, ?, ?>> executorList = Arrays.asList(executor1);
        RiskModelExecuterFactory factory = new RiskModelExecuterFactory(executorList);

        RiskModelExecutor<?, ?, ? extends BaseResult> result = factory.getRiskModelExecuter(ModelName.GAIL);

        assertThat(result).isNull();
    }

    @Test
    @DisplayName("Should handle empty executor list")
    void testConstructorWithEmptyList() {
        List<RiskModelExecutor<?, ?, ?>> emptyList = Arrays.asList();
        RiskModelExecuterFactory factory = new RiskModelExecuterFactory(emptyList);

        assertThat(factory).isNotNull();
        assertThat(factory.getRiskModelExecuter(ModelName.BRCAPRO)).isNull();
    }

    @Test
    @DisplayName("Should return correct executor for multiple models")
    void testGetRiskModelExecuterForMultipleModels() {
        RiskModelExecutor<?, ?, ? extends BaseResult> executor1 = createMockExecutor(ModelName.BRCAPRO);
        RiskModelExecutor<?, ?, ? extends BaseResult> executor2 = createMockExecutor(ModelName.GAIL);
        RiskModelExecutor<?, ?, ? extends BaseResult> executor3 = createMockExecutor(ModelName.MELAPRO);
        List<RiskModelExecutor<?, ?, ?>> executorList = Arrays.asList(executor1, executor2, executor3);
        RiskModelExecuterFactory factory = new RiskModelExecuterFactory(executorList);

        assertThat(factory.getRiskModelExecuter(ModelName.BRCAPRO)).isEqualTo(executor1);
        assertThat(factory.getRiskModelExecuter(ModelName.GAIL)).isEqualTo(executor2);
        assertThat(factory.getRiskModelExecuter(ModelName.MELAPRO)).isEqualTo(executor3);
    }

    private RiskModelExecutor<?, ?, ? extends BaseResult> createMockExecutor(ModelName modelName) {
        return new RiskModelExecutor<Object, Object, BaseResult>() {
            @Override
            public Object transformRequest(com.objex.risk.beans.uno.Risk commonRequest, com.objex.risk.beans.uno.Model model) {
                return null;
            }

            @Override
            public Object invokeRiskModel(Object formattedRequest) {
                return null;
            }

            @Override
            public BaseResult transformResponse(Object modelResponse) {
                return null;
            }

            @Override
            public ModelName getModelName() {
                return modelName;
            }

            @Override
            public com.ambry.transformer.core.validator.RiskModelValidator<Object> getRiskModelValidator() {
                return null;
            }
        };
    }
}

