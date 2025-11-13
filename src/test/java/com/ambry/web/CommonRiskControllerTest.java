package com.ambry.web;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.ambry.service.CommonRiskService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.objex.risk.beans.uno.CombinedResult;
import com.objex.risk.beans.uno.Model;
import com.objex.risk.beans.uno.Model.ModelName;
import com.objex.risk.beans.uno.Risk;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.ambry.IntegrationTest;

@IntegrationTest
@AutoConfigureMockMvc(addFilters = false)
@DisplayName("CommonRiskController Unit Tests")
class CommonRiskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CommonRiskService commonRiskService;

    private Risk risk;
    private CombinedResult combinedResult;

    @BeforeEach
    void setUp() {
        risk = new Risk();
        Model model = new Model();
        model.setName(ModelName.GAIL);
        risk.getRequestedModels().add(model);

        combinedResult = new CombinedResult();
    }

    @Test
    @DisplayName("Should successfully calculate risk models via /api/v1/models/calculate endpoint")
    void testCalculateRiskModelsV1() throws Exception {
        when(commonRiskService.calculateRiskModels(any(Risk.class))).thenReturn(combinedResult);

        // The controller has @RequestMapping({"/api","/risk"}) at class level
        // and @PostMapping("/api/v1/models/calculate") at method level
        // When class has multiple RequestMapping values, Spring creates separate mappings
        // The absolute method path should work, but let's try both /api and /risk base paths
        // Try /risk/api/v1/models/calculate since class has @RequestMapping({"/api","/risk"})
        // and method has absolute path /api/v1/models/calculate
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

        // Try /risk/api/v1/models/calculate since class has @RequestMapping({"/api","/risk"})
        // and method has absolute path /api/v1/models/calculate
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

        // Try /risk/api/v1/models/calculate since class has @RequestMapping({"/api","/risk"})
        // and method has absolute path /api/v1/models/calculate
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

        // Try /risk/api/v1/models/calculate since class has @RequestMapping({"/api","/risk"})
        // and method has absolute path /api/v1/models/calculate
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

        // Try /risk/api/v1/models/calculate since class has @RequestMapping({"/api","/risk"})
        // and method has absolute path /api/v1/models/calculate
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

        // Try /risk/api/v1/models/calculate since class has @RequestMapping({"/api","/risk"})
        // and method has absolute path /api/v1/models/calculate
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

}



