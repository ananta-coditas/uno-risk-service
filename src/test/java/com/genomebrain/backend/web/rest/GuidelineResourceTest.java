package com.genomebrain.backend.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.genomebrain.backend.service.GuideLineService;
import com.genomebrain.backend.service.dto.GenomeBrainResponseDTO;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@ExtendWith(MockitoExtension.class)
@DisplayName("GuidelineResource Unit Tests")
class GuidelineResourceTest {

    @Mock
    private GuideLineService guideLineService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpSession session;

    @InjectMocks
    private GuidelineResource guidelineResource;

    private GenomeBrainResponseDTO expectedResponse;
    private String tags;

    @BeforeEach
    void setUp() {
        tags = "{\"tags\":[\"tag1\",\"tag2\"]}";
        expectedResponse = new GenomeBrainResponseDTO();
        expectedResponse.setStatus("Success");
        expectedResponse.setMessage("Guidelines retrieved successfully");
    }

    @Test
    @DisplayName("Should return guidelines when authorized with valid API key")
    void testGetRecommendedTestsWithValidApiKey() {
        // Arrange
        when(request.getSession()).thenReturn(session);
        when(request.getHeader("Authorization")).thenReturn("APIKey GenomeBrain@123");
        when(guideLineService.getGuidelinesForTags(anyString())).thenReturn(expectedResponse);

        // Act
        ResponseEntity<GenomeBrainResponseDTO> response = guidelineResource.getRecommendedTests(request, tags);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(expectedResponse);
        verify(guideLineService).getGuidelinesForTags(tags);
    }

    @Test
    @DisplayName("Should return unauthorized when API key is invalid")
    void testGetRecommendedTestsWithInvalidApiKey() {
        // Arrange
        when(request.getSession()).thenReturn(session);
        when(request.getHeader("Authorization")).thenReturn("APIKey InvalidKey");

        // Act
        ResponseEntity<GenomeBrainResponseDTO> response = guidelineResource.getRecommendedTests(request, tags);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        GenomeBrainResponseDTO body = response.getBody();
        assertThat(body).isNotNull();
        assertThat(body.getStatus()).isEqualTo("Failure");
        assertThat(body.getMessage()).isEqualTo("Unauthorized access");
        verify(guideLineService, never()).getGuidelinesForTags(anyString());
    }

    @Test
    @DisplayName("Should return unauthorized when Authorization header is missing")
    void testGetRecommendedTestsWithMissingAuthorizationHeader() {
        // Arrange
        when(request.getSession()).thenReturn(session);
        when(request.getHeader("Authorization")).thenReturn(null);

        // Act
        ResponseEntity<GenomeBrainResponseDTO> response = guidelineResource.getRecommendedTests(request, tags);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        GenomeBrainResponseDTO body = response.getBody();
        assertThat(body).isNotNull();
        assertThat(body.getStatus()).isEqualTo("Failure");
        assertThat(body.getMessage()).isEqualTo("Unauthorized access");
        verify(guideLineService, never()).getGuidelinesForTags(anyString());
    }

    @Test
    @DisplayName("Should return unauthorized when Authorization header does not contain APIKey")
    void testGetRecommendedTestsWithAuthorizationHeaderWithoutApiKey() {
        // Arrange
        when(request.getSession()).thenReturn(session);
        when(request.getHeader("Authorization")).thenReturn("Bearer token123");

        // Act
        ResponseEntity<GenomeBrainResponseDTO> response = guidelineResource.getRecommendedTests(request, tags);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        GenomeBrainResponseDTO body = response.getBody();
        assertThat(body).isNotNull();
        assertThat(body.getStatus()).isEqualTo("Failure");
        assertThat(body.getMessage()).isEqualTo("Unauthorized access");
        verify(guideLineService, never()).getGuidelinesForTags(anyString());
    }

    @Test
    @DisplayName("Should return unauthorized when session is null")
    void testGetRecommendedTestsWithNullSession() {
        // Arrange
        when(request.getSession()).thenReturn(null);

        // Act
        ResponseEntity<GenomeBrainResponseDTO> response = guidelineResource.getRecommendedTests(request, tags);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        GenomeBrainResponseDTO body = response.getBody();
        assertThat(body).isNotNull();
        assertThat(body.getStatus()).isEqualTo("Failure");
        assertThat(body.getMessage()).isEqualTo("Unauthorized access");
        verify(guideLineService, never()).getGuidelinesForTags(anyString());
    }

    @Test
    @DisplayName("Should handle case-insensitive API key")
    void testGetRecommendedTestsWithCaseInsensitiveApiKey() {
        // Arrange
        when(request.getSession()).thenReturn(session);
        when(request.getHeader("Authorization")).thenReturn("APIKey genomebrain@123");
        when(guideLineService.getGuidelinesForTags(anyString())).thenReturn(expectedResponse);

        // Act
        ResponseEntity<GenomeBrainResponseDTO> response = guidelineResource.getRecommendedTests(request, tags);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(expectedResponse);
        verify(guideLineService).getGuidelinesForTags(tags);
    }

    @Test
    @DisplayName("Should handle API key with single space")
    void testGetRecommendedTestsWithApiKeyWithSingleSpace() {
        // Arrange
        when(request.getSession()).thenReturn(session);
        when(request.getHeader("Authorization")).thenReturn("APIKey GenomeBrain@123");
        when(guideLineService.getGuidelinesForTags(anyString())).thenReturn(expectedResponse);

        // Act
        ResponseEntity<GenomeBrainResponseDTO> response = guidelineResource.getRecommendedTests(request, tags);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(expectedResponse);
        verify(guideLineService).getGuidelinesForTags(tags);
    }

    @Test
    @DisplayName("Should reject API key with extra spaces")
    void testGetRecommendedTestsWithApiKeyWithExtraSpaces() {
        // Arrange
        when(request.getSession()).thenReturn(session);
        when(request.getHeader("Authorization")).thenReturn("APIKey  GenomeBrain@123");

        // Act
        ResponseEntity<GenomeBrainResponseDTO> response = guidelineResource.getRecommendedTests(request, tags);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getStatus()).isEqualTo("Failure");
        assertThat(response.getBody().getMessage()).isEqualTo("Unauthorized access");
        verify(guideLineService, never()).getGuidelinesForTags(anyString());
    }

    @Test
    @DisplayName("Should pass tags to service correctly")
    void testGetRecommendedTestsPassesTagsToService() {
        // Arrange
        String customTags = "{\"tags\":[\"customTag1\",\"customTag2\"]}";
        when(request.getSession()).thenReturn(session);
        when(request.getHeader("Authorization")).thenReturn("APIKey GenomeBrain@123");
        when(guideLineService.getGuidelinesForTags(anyString())).thenReturn(expectedResponse);

        // Act
        guidelineResource.getRecommendedTests(request, customTags);

        // Assert
        verify(guideLineService).getGuidelinesForTags(customTags);
    }

    @Test
    @DisplayName("Should return service response when authorized")
    void testGetRecommendedTestsReturnsServiceResponse() {
        // Arrange
        GenomeBrainResponseDTO serviceResponse = new GenomeBrainResponseDTO();
        serviceResponse.setStatus("Success");
        serviceResponse.setMessage("Test message");
        serviceResponse.setHBOC(true);
        serviceResponse.setLYNCH(false);
        
        when(request.getSession()).thenReturn(session);
        when(request.getHeader("Authorization")).thenReturn("APIKey GenomeBrain@123");
        when(guideLineService.getGuidelinesForTags(anyString())).thenReturn(serviceResponse);

        // Act
        ResponseEntity<GenomeBrainResponseDTO> response = guidelineResource.getRecommendedTests(request, tags);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(serviceResponse);
        assertThat(response.getBody().getStatus()).isEqualTo("Success");
        assertThat(response.getBody().getMessage()).isEqualTo("Test message");
        assertThat(response.getBody().isHBOC()).isTrue();
        assertThat(response.getBody().isLYNCH()).isFalse();
    }
}

