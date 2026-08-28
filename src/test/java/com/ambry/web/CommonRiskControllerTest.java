package com.ambry.web;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.ambry.service.CommonRiskService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.objex.risk.beans.BaseResult;
import com.objex.risk.beans.uno.CombinedResult;
import com.objex.risk.beans.uno.Model;
import com.objex.risk.beans.uno.Model.ModelName;
import com.objex.risk.beans.uno.Risk;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.info.BuildProperties;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.ambry.IntegrationTest;

import java.time.Instant;
import java.util.Map;

@IntegrationTest
@AutoConfigureMockMvc(addFilters = false)
@DisplayName("CommonRiskController Unit Tests")
class CommonRiskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CommonRiskService commonRiskService;

    @MockitoBean
    private BuildProperties buildProperties;

    private Risk risk;
    private CombinedResult combinedResult;

    @BeforeEach
    void setUp() {
        risk = new Risk();
        Model model = new Model();
        model.setName(ModelName.GAIL);
        risk.getRequestedModels().add(model);

        combinedResult = new CombinedResult();
        
        // Setup default BuildProperties mock
        when(buildProperties.getName()).thenReturn("uno-risk-service");
        when(buildProperties.getVersion()).thenReturn("1.0.0");
        when(buildProperties.getTime()).thenReturn(Instant.now());
    }

    @Test
    @DisplayName("Should successfully calculate risk models via /risk/api/v1/models/calculate endpoint")
    void testCalculateRiskModelsV1() throws Exception {
        when(commonRiskService.calculateRiskModels(any(Risk.class))).thenReturn(combinedResult);

        // The controller has @RequestMapping({"/risk"}) at class level
        // and @PostMapping("/api/v1/models/calculate") at method level
        // Spring combines them: /risk + /api/v1/models/calculate = /risk/api/v1/models/calculate
        MvcResult result = mockMvc.perform(post("/risk/api/v1/models/calculate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(risk))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn();

        assertNotNull(result);
        verify(commonRiskService).calculateRiskModels(any(Risk.class));
    }

    @Test
    @DisplayName("Should successfully calculate risk models with default headers")
    void testCalculateRiskModelsV1WithDefaultHeaders() throws Exception {
        when(commonRiskService.calculateRiskModels(any(Risk.class))).thenReturn(combinedResult);

        // The method mapping is absolute (starts with /), so the path is /risk/api/v1/models/calculate
        MvcResult result = mockMvc.perform(post("/risk/api/v1/models/calculate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(risk))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn();

        assertNotNull(result);
        verify(commonRiskService).calculateRiskModels(any(Risk.class));
    }

    @Test
    @DisplayName("Should successfully calculate risk models with custom headers")
    void testCalculateRiskModelsV1WithCustomHeaders() throws Exception {
        when(commonRiskService.calculateRiskModels(any(Risk.class))).thenReturn(combinedResult);

        // The method mapping is absolute (starts with /), so the path is /risk/api/v1/models/calculate
        MvcResult result = mockMvc.perform(post("/risk/api/v1/models/calculate")
                .header("Authorization", "Bearer custom-token")
                .header("Source", "CustomSource")
                .header("TraceId", "custom-trace-id")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(risk))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn();

        assertNotNull(result);
        verify(commonRiskService).calculateRiskModels(any(Risk.class));
    }

    @Test
    @DisplayName("Should handle invalid request body")
    void testCalculateRiskModelsInvalidRequestBody() throws Exception {
        mockMvc.perform(post("/risk/api/v1/models/calculate")
                .contentType(MediaType.APPLICATION_JSON)
                .content("invalid json")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should handle null request body")
    void testCalculateRiskModelsNullRequestBody() throws Exception {
        mockMvc.perform(post("/risk/api/v1/models/calculate")
                .contentType(MediaType.APPLICATION_JSON)
                .content("")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should handle service exception")
    void testCalculateRiskModelsServiceException() throws Exception {
        when(commonRiskService.calculateRiskModels(any(Risk.class)))
            .thenThrow(new RuntimeException("Service error"));

        mockMvc.perform(post("/risk/api/v1/models/calculate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(risk))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is5xxServerError());

        verify(commonRiskService).calculateRiskModels(any(Risk.class));
    }

    @Test
    @DisplayName("Should handle empty risk object")
    void testCalculateRiskModelsEmptyRisk() throws Exception {
        Risk emptyRisk = new Risk();
        when(commonRiskService.calculateRiskModels(any(Risk.class))).thenReturn(combinedResult);

        // The method mapping is absolute (starts with /), so the path is /risk/api/v1/models/calculate
        MvcResult result = mockMvc.perform(post("/risk/api/v1/models/calculate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(emptyRisk))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn();

        assertNotNull(result);
        verify(commonRiskService).calculateRiskModels(any(Risk.class));
    }

    @Test
    @DisplayName("Should handle missing content type")
    void testCalculateRiskModelsMissingContentType() throws Exception {
        mockMvc.perform(post("/risk/api/v1/models/calculate")
                .content(objectMapper.writeValueAsString(risk))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnsupportedMediaType());
    }

    @Test
    @DisplayName("Should handle multiple models in single request")
    void testCalculateRiskModelsMultipleModels() throws Exception {
        Risk multiModelRisk = new Risk();
        Model gailModel = new Model();
        gailModel.setName(ModelName.GAIL);
        multiModelRisk.getRequestedModels().add(gailModel);

        Model clausModel = new Model();
        clausModel.setName(ModelName.CLAUS);
        multiModelRisk.getRequestedModels().add(clausModel);

        when(commonRiskService.calculateRiskModels(any(Risk.class))).thenReturn(combinedResult);

        // The method mapping is absolute (starts with /), so the path is /risk/api/v1/models/calculate
        MvcResult result = mockMvc.perform(post("/risk/api/v1/models/calculate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(multiModelRisk))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn();

        assertNotNull(result);
        verify(commonRiskService).calculateRiskModels(any(Risk.class));
    }

    @Test
    @DisplayName("Should handle different model types - BRCAPRO")
    void testCalculateRiskModelsBRCAPROModel() throws Exception {
        Risk brcaRisk = new Risk();
        Model brcaModel = new Model();
        brcaModel.setName(ModelName.BRCAPRO);
        brcaRisk.getRequestedModels().add(brcaModel);

        when(commonRiskService.calculateRiskModels(any(Risk.class))).thenReturn(combinedResult);

        mockMvc.perform(post("/risk/api/v1/models/calculate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(brcaRisk))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

        verify(commonRiskService).calculateRiskModels(any(Risk.class));
    }

    @Test
    @DisplayName("Should handle different model types - PREMM")
    void testCalculateRiskModelsPREMMModel() throws Exception {
        Risk premmRisk = new Risk();
        Model premmModel = new Model();
        premmModel.setName(ModelName.PREMM);
        premmRisk.getRequestedModels().add(premmModel);

        when(commonRiskService.calculateRiskModels(any(Risk.class))).thenReturn(combinedResult);

        mockMvc.perform(post("/risk/api/v1/models/calculate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(premmRisk))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

        verify(commonRiskService).calculateRiskModels(any(Risk.class));
    }

    @Test
    @DisplayName("Should handle different model types - TYRER")
    void testCalculateRiskModelsTYRERModel() throws Exception {
        Risk tyrerRisk = new Risk();
        Model tyrerModel = new Model();
        tyrerModel.setName(ModelName.TYRER);
        tyrerRisk.getRequestedModels().add(tyrerModel);

        when(commonRiskService.calculateRiskModels(any(Risk.class))).thenReturn(combinedResult);

        mockMvc.perform(post("/risk/api/v1/models/calculate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(tyrerRisk))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

        verify(commonRiskService).calculateRiskModels(any(Risk.class));
    }

    @Test
    @DisplayName("Should handle Authorization header with different formats")
    void testCalculateRiskModelsDifferentAuthFormats() throws Exception {
        when(commonRiskService.calculateRiskModels(any(Risk.class))).thenReturn(combinedResult);

        // Test with Bearer token
        mockMvc.perform(post("/risk/api/v1/models/calculate")
                .header("Authorization", "Bearer token123")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(risk))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        // Test with custom token format
        mockMvc.perform(post("/risk/api/v1/models/calculate")
                .header("Authorization", "Custom token123")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(risk))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(commonRiskService, times(2)).calculateRiskModels(any(Risk.class));
    }

    @Test
    @DisplayName("Should handle missing optional headers")
    void testCalculateRiskModelsMissingOptionalHeaders() throws Exception {
        when(commonRiskService.calculateRiskModels(any(Risk.class))).thenReturn(combinedResult);

        mockMvc.perform(post("/risk/api/v1/models/calculate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(risk))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

        verify(commonRiskService).calculateRiskModels(any(Risk.class));
    }

    @Test
    @DisplayName("Should handle malformed JSON")
    void testCalculateRiskModelsMalformedJson() throws Exception {
        mockMvc.perform(post("/risk/api/v1/models/calculate")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{invalid: json}")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should handle request with only Authorization header")
    void testCalculateRiskModelsOnlyAuthorizationHeader() throws Exception {
        when(commonRiskService.calculateRiskModels(any(Risk.class))).thenReturn(combinedResult);

        mockMvc.perform(post("/risk/api/v1/models/calculate")
                .header("Authorization", "Bearer token")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(risk))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(commonRiskService).calculateRiskModels(any(Risk.class));
    }

    @Test
    @DisplayName("Should handle request with only Source header")
    void testCalculateRiskModelsOnlySourceHeader() throws Exception {
        when(commonRiskService.calculateRiskModels(any(Risk.class))).thenReturn(combinedResult);

        mockMvc.perform(post("/risk/api/v1/models/calculate")
                .header("Source", "TestSource")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(risk))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(commonRiskService).calculateRiskModels(any(Risk.class));
    }

    @Test
    @DisplayName("Should handle request with only TraceId header")
    void testCalculateRiskModelsOnlyTraceIdHeader() throws Exception {
        when(commonRiskService.calculateRiskModels(any(Risk.class))).thenReturn(combinedResult);

        mockMvc.perform(post("/risk/api/v1/models/calculate")
                .header("TraceId", "test-trace-123")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(risk))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(commonRiskService).calculateRiskModels(any(Risk.class));
    }

    @Test
    @DisplayName("Should verify service is called with correct Risk object")
    void testCalculateRiskModelsServiceCalledWithCorrectRisk() throws Exception {
        Risk testRisk = new Risk();
        Model testModel = new Model();
        testModel.setName(ModelName.GAIL);
        testModel.setAgeBy(5);
        testModel.setAgeTo(90);
        testRisk.getRequestedModels().add(testModel);
        testRisk.setFamilyId("TEST_FAMILY_001");

        when(commonRiskService.calculateRiskModels(any(Risk.class))).thenReturn(combinedResult);

        mockMvc.perform(post("/risk/api/v1/models/calculate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testRisk))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(commonRiskService).calculateRiskModels(org.mockito.ArgumentMatchers.argThat(risk -> 
            risk != null && 
            risk.getRequestedModels() != null && 
            !risk.getRequestedModels().isEmpty() &&
            risk.getRequestedModels().get(0).getName() == ModelName.GAIL
        ));
    }

    @Test
    @DisplayName("Should return CombinedResult in response")
    void testCalculateRiskModelsReturnsCombinedResult() throws Exception {
        when(commonRiskService.calculateRiskModels(any(Risk.class))).thenReturn(combinedResult);

        // The method mapping is absolute (starts with /), so the path is /risk/api/v1/models/calculate
        MvcResult result = mockMvc.perform(post("/risk/api/v1/models/calculate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(risk))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn();

        String responseContent = result.getResponse().getContentAsString();
        assertNotNull(responseContent);
        
        CombinedResult resultObj = objectMapper.readValue(responseContent, CombinedResult.class);
        assertNotNull(resultObj);
    }

    @Test
    @DisplayName("Should handle IllegalArgumentException from service")
    void testCalculateRiskModelsIllegalArgumentException() throws Exception {
        when(commonRiskService.calculateRiskModels(any(Risk.class)))
            .thenThrow(new IllegalArgumentException("Invalid argument"));

        mockMvc.perform(post("/risk/api/v1/models/calculate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(risk))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is5xxServerError());

        verify(commonRiskService).calculateRiskModels(any(Risk.class));
    }

    @Test
    @DisplayName("Should handle NullPointerException from service")
    void testCalculateRiskModelsNullPointerException() throws Exception {
        when(commonRiskService.calculateRiskModels(any(Risk.class)))
            .thenThrow(new NullPointerException("Null value encountered"));

        mockMvc.perform(post("/risk/api/v1/models/calculate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(risk))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is5xxServerError());

        verify(commonRiskService).calculateRiskModels(any(Risk.class));
    }

    @Test
    @DisplayName("Should return version information successfully")
    void testVersion() throws Exception {
        when(buildProperties.getName()).thenReturn("uno-risk-service");
        when(buildProperties.getVersion()).thenReturn("1.0.0-SNAPSHOT");
        when(buildProperties.getTime()).thenReturn(Instant.parse("2024-01-01T00:00:00Z"));

        MvcResult result = mockMvc.perform(get("/risk/version")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn();

        String responseContent = result.getResponse().getContentAsString();
        assertNotNull(responseContent);
        
        @SuppressWarnings("unchecked")
        Map<String, Object> versionInfo = objectMapper.readValue(responseContent, Map.class);
        assertNotNull(versionInfo);
        assertEquals("uno-risk-service", versionInfo.get("service"));
        assertEquals("1.0.0-SNAPSHOT", versionInfo.get("version"));
        assertNotNull(versionInfo.get("builtAt"));
    }

    @Test
    @DisplayName("Should return version information with different build properties")
    void testVersionWithDifferentBuildProperties() throws Exception {
        when(buildProperties.getName()).thenReturn("test-service");
        when(buildProperties.getVersion()).thenReturn("2.5.3");
        when(buildProperties.getTime()).thenReturn(Instant.parse("2024-12-31T23:59:59Z"));

        MvcResult result = mockMvc.perform(get("/risk/version")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        @SuppressWarnings("unchecked")
        Map<String, Object> versionInfo = objectMapper.readValue(result.getResponse().getContentAsString(), Map.class);
        assertEquals("test-service", versionInfo.get("service"));
        assertEquals("2.5.3", versionInfo.get("version"));
        assertTrue(versionInfo.get("builtAt").toString().contains("2024"));
    }

    @Test
    @DisplayName("Should handle version endpoint with different accept types")
    void testVersionWithDifferentAcceptTypes() throws Exception {
        // Ensure BuildProperties is mocked for this test
        when(buildProperties.getName()).thenReturn("uno-risk-service");
        when(buildProperties.getVersion()).thenReturn("1.0.0");
        when(buildProperties.getTime()).thenReturn(Instant.now());
        
        mockMvc.perform(get("/risk/version")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    @DisplayName("Should test TestResult class getModel method")
    void testTestResultGetModel() {
        // TestResult is a package-private class in the same file
        // Use reflection to test it
        try {
            Class<?> testResultClass = Class.forName("com.ambry.web.TestResult");
            BaseResult testResult = (BaseResult) testResultClass.getDeclaredConstructor().newInstance();
            
            String model = testResult.getModel();
            assertEquals("OK", model);
        } catch (Exception e) {
            throw new AssertionError("Could not test TestResult.getModel(): " + e.getMessage(), e);
        }
    }

    @Test
    @DisplayName("Should test TestResult class getColumnToFields method")
    void testTestResultGetColumnToFields() {
        // TestResult is a package-private class in the same file
        // Use reflection to test it
        try {
            Class<?> testResultClass = Class.forName("com.ambry.web.TestResult");
            BaseResult testResult = (BaseResult) testResultClass.getDeclaredConstructor().newInstance();
            
            Map<String, String> columnToFields = testResult.getColumnToFields();
            assertNotNull(columnToFields);
            assertTrue(columnToFields.isEmpty());
        } catch (Exception e) {
            throw new AssertionError("Could not test TestResult.getColumnToFields(): " + e.getMessage(), e);
        }
    }

    @Test
    @DisplayName("Should handle calculateRiskModels with all headers present")
    void testCalculateRiskModelsWithAllHeaders() throws Exception {
        when(commonRiskService.calculateRiskModels(any(Risk.class))).thenReturn(combinedResult);

        MvcResult result = mockMvc.perform(post("/risk/api/v1/models/calculate")
                .header("Authorization", "Bearer test-token")
                .header("Source", "TestSource")
                .header("TraceId", "test-trace-id")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(risk))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn();

        assertNotNull(result);
        verify(commonRiskService).calculateRiskModels(any(Risk.class));
    }

    @Test
    @DisplayName("Should handle calculateRiskModels with default header values")
    void testCalculateRiskModelsWithDefaultHeaderValues() throws Exception {
        when(commonRiskService.calculateRiskModels(any(Risk.class))).thenReturn(combinedResult);

        // Don't provide headers to test default values
        MvcResult result = mockMvc.perform(post("/risk/api/v1/models/calculate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(risk))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        assertNotNull(result);
        verify(commonRiskService).calculateRiskModels(any(Risk.class));
    }

    @Test
    @DisplayName("Should handle validation errors in request body")
    void testCalculateRiskModelsWithValidationErrors() throws Exception {
        // Create an invalid Risk object (if validation exists)
        Risk invalidRisk = new Risk();
        // Risk object might have validation constraints
        
        when(commonRiskService.calculateRiskModels(any(Risk.class))).thenReturn(combinedResult);

        mockMvc.perform(post("/risk/api/v1/models/calculate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRisk))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()); // Or isBadRequest() if validation is enabled
    }

}



