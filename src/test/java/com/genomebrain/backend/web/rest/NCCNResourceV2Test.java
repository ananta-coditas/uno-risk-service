package com.genomebrain.backend.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.genomebrain.backend.service.NCCNExecuter;
import com.genomebrain.backend.service.dto.CustomResponseDTO;
import com.genomebrain.backend.service.dto.NCCNCoreRequest;
import com.genomebrain.backend.service.dto.NCCNCoreResponse;

@ExtendWith(MockitoExtension.class)
@DisplayName("NCCNResourceV2 Unit Tests")
class NCCNResourceV2Test {

    @Mock
    private NCCNExecuter nonCareQuestionnaire;

    @InjectMocks
    private NCCNResourceV2 nccnResourceV2;

    private NCCNCoreRequest request;
    private CustomResponseDTO expectedResponse;
    private NCCNCoreResponse nccnResponse;

    @BeforeEach
    void setUp() {
        request = new NCCNCoreRequest();
        nccnResponse = new NCCNCoreResponse();
        nccnResponse.setHbocOverall("MET");
        nccnResponse.setLynchOverall("NOT_MET");
        
        expectedResponse = new CustomResponseDTO();
        expectedResponse.setStatus("SUCCESS");
        expectedResponse.setStatusCode(200);
        expectedResponse.setMessage("NCCN evaluation completed");
        expectedResponse.setData(nccnResponse);
    }

    @Test
    @DisplayName("Should evaluate NCCN and return response")
    void testEvaluateNCCN() {
        // Arrange
        when(nonCareQuestionnaire.getNonCareQuestionnaireDTO(any(NCCNCoreRequest.class))).thenReturn(expectedResponse);

        // Act
        CustomResponseDTO result = nccnResourceV2.evaluateNCCN(request);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(expectedResponse);
        assertThat(result.getStatus()).isEqualTo("SUCCESS");
        assertThat(result.getStatusCode()).isEqualTo(200);
        assertThat(result.getMessage()).isEqualTo("NCCN evaluation completed");
        verify(nonCareQuestionnaire).getNonCareQuestionnaireDTO(request);
    }

    @Test
    @DisplayName("Should pass request to service correctly")
    void testEvaluateNCCNPassesRequestToService() {
        // Arrange
        NCCNCoreRequest customRequest = new NCCNCoreRequest();
        when(nonCareQuestionnaire.getNonCareQuestionnaireDTO(any(NCCNCoreRequest.class))).thenReturn(expectedResponse);

        // Act
        nccnResourceV2.evaluateNCCN(customRequest);

        // Assert
        verify(nonCareQuestionnaire).getNonCareQuestionnaireDTO(customRequest);
    }

    @Test
    @DisplayName("Should return service response with data")
    void testEvaluateNCCNReturnsServiceResponseWithData() {
        // Arrange
        NCCNCoreResponse responseData = new NCCNCoreResponse();
        responseData.setHbocOverall("MET");
        responseData.setLynchOverall("MET");
        responseData.setApsApcOverall("NOT_MET");
        
        CustomResponseDTO serviceResponse = new CustomResponseDTO();
        serviceResponse.setStatus("SUCCESS");
        serviceResponse.setStatusCode(200);
        serviceResponse.setData(responseData);
        
        when(nonCareQuestionnaire.getNonCareQuestionnaireDTO(any(NCCNCoreRequest.class))).thenReturn(serviceResponse);

        // Act
        CustomResponseDTO result = nccnResourceV2.evaluateNCCN(request);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getStatus()).isEqualTo("SUCCESS");
        assertThat(result.getData()).isNotNull();
        assertThat(result.getData()).isInstanceOf(NCCNCoreResponse.class);
        NCCNCoreResponse data = (NCCNCoreResponse) result.getData();
        assertThat(data.getHbocOverall()).isEqualTo("MET");
        assertThat(data.getLynchOverall()).isEqualTo("MET");
        assertThat(data.getApsApcOverall()).isEqualTo("NOT_MET");
    }

    @Test
    @DisplayName("Should handle null request")
    void testEvaluateNCCNWithNullRequest() {
        // Arrange
        CustomResponseDTO nullResponse = new CustomResponseDTO();
        nullResponse.setStatus("FAILURE");
        nullResponse.setStatusCode(400);
        when(nonCareQuestionnaire.getNonCareQuestionnaireDTO(any())).thenReturn(nullResponse);

        // Act
        CustomResponseDTO result = nccnResourceV2.evaluateNCCN(null);

        // Assert
        assertThat(result).isNotNull();
        verify(nonCareQuestionnaire).getNonCareQuestionnaireDTO(null);
    }

    @Test
    @DisplayName("Should handle failure response from service")
    void testEvaluateNCCNWithFailureResponse() {
        // Arrange
        CustomResponseDTO failureResponse = new CustomResponseDTO();
        failureResponse.setStatus("FAILURE");
        failureResponse.setStatusCode(400);
        failureResponse.setMessage("Validation failed");
        
        when(nonCareQuestionnaire.getNonCareQuestionnaireDTO(any(NCCNCoreRequest.class))).thenReturn(failureResponse);

        // Act
        CustomResponseDTO result = nccnResourceV2.evaluateNCCN(request);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getStatus()).isEqualTo("FAILURE");
        assertThat(result.getStatusCode()).isEqualTo(400);
        assertThat(result.getMessage()).isEqualTo("Validation failed");
    }

    @Test
    @DisplayName("Should return response with null data when service returns null data")
    void testEvaluateNCCNWithNullData() {
        // Arrange
        CustomResponseDTO responseWithNullData = new CustomResponseDTO();
        responseWithNullData.setStatus("SUCCESS");
        responseWithNullData.setStatusCode(200);
        responseWithNullData.setData(null);
        
        when(nonCareQuestionnaire.getNonCareQuestionnaireDTO(any(NCCNCoreRequest.class))).thenReturn(responseWithNullData);

        // Act
        CustomResponseDTO result = nccnResourceV2.evaluateNCCN(request);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getStatus()).isEqualTo("SUCCESS");
        assertThat(result.getData()).isNull();
    }

    @Test
    @DisplayName("Should handle different request objects")
    void testEvaluateNCCNWithDifferentRequests() {
        // Arrange
        NCCNCoreRequest request1 = new NCCNCoreRequest();
        NCCNCoreRequest request2 = new NCCNCoreRequest();
        
        CustomResponseDTO response1 = new CustomResponseDTO();
        response1.setStatus("SUCCESS");
        response1.setStatusCode(200);
        
        CustomResponseDTO response2 = new CustomResponseDTO();
        response2.setStatus("SUCCESS");
        response2.setStatusCode(200);
        
        when(nonCareQuestionnaire.getNonCareQuestionnaireDTO(request1)).thenReturn(response1);
        when(nonCareQuestionnaire.getNonCareQuestionnaireDTO(request2)).thenReturn(response2);

        // Act
        CustomResponseDTO result1 = nccnResourceV2.evaluateNCCN(request1);
        CustomResponseDTO result2 = nccnResourceV2.evaluateNCCN(request2);

        // Assert
        assertThat(result1).isEqualTo(response1);
        assertThat(result2).isEqualTo(response2);
        verify(nonCareQuestionnaire).getNonCareQuestionnaireDTO(request1);
        verify(nonCareQuestionnaire).getNonCareQuestionnaireDTO(request2);
    }
}

