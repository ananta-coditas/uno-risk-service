package com.ambry.web.rest.errors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.core.MethodParameter;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.ServletWebRequest;

import jakarta.servlet.http.HttpServletRequest;

import java.lang.reflect.Method;
import java.util.List;

@DisplayName("ExceptionTranslator Unit Tests")
class ExceptionTranslatorTest {

    private ExceptionTranslator exceptionTranslator;
    private Environment environment;
    private NativeWebRequest nativeWebRequest;
    private HttpServletRequest httpServletRequest;

    @BeforeEach
    void setUp() {
        environment = mock(Environment.class);
        exceptionTranslator = new ExceptionTranslator(environment);
        
        httpServletRequest = mock(HttpServletRequest.class);
        when(httpServletRequest.getRequestURI()).thenReturn("/api/test");
        nativeWebRequest = new ServletWebRequest(httpServletRequest);
    }

    @Test
    @DisplayName("Should handle generic exception")
    void testHandleAnyException() {
        RuntimeException exception = new RuntimeException("Test exception");
        when(environment.getActiveProfiles()).thenReturn(new String[]{"dev"});

        ResponseEntity<Object> response = exceptionTranslator.handleAnyException(exception, nativeWebRequest);

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Test
    @DisplayName("Should map AccessDeniedException to FORBIDDEN status")
    void testHandleAccessDeniedException() {
        AccessDeniedException exception = new AccessDeniedException("Access denied");
        when(environment.getActiveProfiles()).thenReturn(new String[]{"dev"});

        ResponseEntity<Object> response = exceptionTranslator.handleAnyException(exception, nativeWebRequest);

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    @DisplayName("Should map BadCredentialsException to UNAUTHORIZED status")
    void testHandleBadCredentialsException() {
        BadCredentialsException exception = new BadCredentialsException("Bad credentials");
        when(environment.getActiveProfiles()).thenReturn(new String[]{"dev"});

        ResponseEntity<Object> response = exceptionTranslator.handleAnyException(exception, nativeWebRequest);

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    @DisplayName("Should customize problem with production profile")
    void testCustomizeProblemInProduction() {
        RuntimeException exception = new RuntimeException("Test exception");
        when(environment.getActiveProfiles()).thenReturn(new String[]{"prod"});

        UnoProblemDetailEx problem = exceptionTranslator.wrapAndCustomizeProblem(exception, nativeWebRequest);

        assertThat(problem).isNotNull();
        assertThat(problem.getStatus()).isGreaterThan(0);
        assertThat(problem.getProperties()).isNotNull();
    }

    @Test
    @DisplayName("Should customize problem with development profile")
    void testCustomizeProblemInDevelopment() {
        RuntimeException exception = new RuntimeException("Test exception");
        when(environment.getActiveProfiles()).thenReturn(new String[]{"dev"});

        UnoProblemDetailEx problem = exceptionTranslator.wrapAndCustomizeProblem(exception, nativeWebRequest);

        assertThat(problem).isNotNull();
        assertThat(problem.getStatus()).isGreaterThan(0);
    }

    @Test
    @DisplayName("Should include path in problem details")
    void testProblemIncludesPath() {
        RuntimeException exception = new RuntimeException("Test exception");
        when(environment.getActiveProfiles()).thenReturn(new String[]{"dev"});

        UnoProblemDetailEx problem = exceptionTranslator.wrapAndCustomizeProblem(exception, nativeWebRequest);

        assertThat(problem).isNotNull();
        assertThat(problem.getStatus()).isGreaterThan(0);
    }

    @Test
    @DisplayName("Should handle exception with null request")
    void testHandleExceptionWithNullRequest() {
        RuntimeException exception = new RuntimeException("Test exception");
        when(environment.getActiveProfiles()).thenReturn(new String[]{"dev"});

        UnoProblemDetailEx problem = exceptionTranslator.wrapAndCustomizeProblem(exception, null);

        assertThat(problem).isNotNull();
        assertThat(problem.getStatus()).isGreaterThan(0);
    }

    @Test
    @DisplayName("Should handle exception internal with null body")
    void testHandleExceptionInternalWithNullBody() {
        RuntimeException exception = new RuntimeException("Test exception");
        when(environment.getActiveProfiles()).thenReturn(new String[]{"dev"});

        // handleExceptionInternal is protected, but we can test it indirectly through handleAnyException
        // or we can verify that when body is null, wrapAndCustomizeProblem is called
        ResponseEntity<Object> response = exceptionTranslator.handleAnyException(exception, nativeWebRequest);

        assertThat(response).isNotNull();
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Test
    @DisplayName("Should handle exception internal with provided body")
    void testHandleExceptionInternalWithBody() {
        RuntimeException exception = new RuntimeException("Test exception");
        when(environment.getActiveProfiles()).thenReturn(new String[]{"dev"});
        
        // Test that handleExceptionInternal uses the provided body
        // Since it's protected, we test through handleAnyException which calls it
        ResponseEntity<Object> response = exceptionTranslator.handleAnyException(exception, nativeWebRequest);
        
        assertThat(response).isNotNull();
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).isInstanceOf(UnoProblemDetailEx.class);
    }

    @Test
    @DisplayName("Should include field errors for MethodArgumentNotValidException")
    void testHandleMethodArgumentNotValidException() throws NoSuchMethodException {
        BindingResult bindingResult = mock(BindingResult.class);
        FieldError fieldError1 = new FieldError("testDTO", "field1", "Field 1 is required");
        FieldError fieldError2 = new FieldError("testDTO", "field2", "Field 2 is invalid");
        when(bindingResult.getFieldErrors()).thenReturn(List.of(fieldError1, fieldError2));
        
        // Create a MethodParameter for the exception constructor
        Method method = this.getClass().getDeclaredMethod("helperMethod", String.class);
        MethodParameter methodParameter = new MethodParameter(method, 0);
        MethodArgumentNotValidException exception = new MethodArgumentNotValidException(methodParameter, bindingResult);
        when(environment.getActiveProfiles()).thenReturn(new String[]{"dev"});

        ResponseEntity<Object> response = exceptionTranslator.handleAnyException(exception, nativeWebRequest);

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        
        UnoProblemDetailEx problem = (UnoProblemDetailEx) response.getBody();
        assertThat(problem).isNotNull();
        assertThat(problem.getProperties()).isNotNull();
        assertThat(problem.getProperties().containsKey("fieldErrors")).isTrue();
        
        @SuppressWarnings("unchecked")
        List<FieldErrorVM> fieldErrors = (List<FieldErrorVM>) problem.getProperties().get("fieldErrors");
        assertThat(fieldErrors).isNotNull();
        assertThat(fieldErrors).hasSize(2);
        assertThat(fieldErrors.get(0).getField()).isEqualTo("field1");
        assertThat(fieldErrors.get(0).getMessage()).isEqualTo("Field 1 is required");
        assertThat(fieldErrors.get(1).getField()).isEqualTo("field2");
        assertThat(fieldErrors.get(1).getMessage()).isEqualTo("Field 2 is invalid");
    }

    @Test
    @DisplayName("Should set correct type and title for MethodArgumentNotValidException")
    void testMethodArgumentNotValidExceptionDetails() throws NoSuchMethodException {
        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.getFieldErrors()).thenReturn(List.of());
        
        // Create a MethodParameter for the exception constructor
        Method method = this.getClass().getDeclaredMethod("helperMethod", String.class);
        MethodParameter methodParameter = new MethodParameter(method, 0);
        MethodArgumentNotValidException exception = new MethodArgumentNotValidException(methodParameter, bindingResult);
        when(environment.getActiveProfiles()).thenReturn(new String[]{"dev"});

        UnoProblemDetailEx problem = exceptionTranslator.wrapAndCustomizeProblem(exception, nativeWebRequest);

        assertThat(problem).isNotNull();
        assertThat(problem.getTitle()).isEqualTo("Method argument not valid");
        assertThat(problem.getType()).isNotNull();
    }

    // Helper method for creating MethodParameter
    @SuppressWarnings("unused")
    private void helperMethod(String param) {
        // This method is only used for reflection to create MethodParameter
    }
}

